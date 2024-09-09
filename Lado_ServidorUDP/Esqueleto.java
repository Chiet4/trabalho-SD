package com.anchietaalbano.trabalho;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;
import java.util.logging.Logger;

public class Esqueleto {
    private Passagem passagem;
    private static final Logger logger = Logger.getLogger(Esqueleto.class.getName());

    public Esqueleto() {
        this.passagem = new Passagem(); // Instancia a classe Passagem
    }

    public String reservar_ticket(JsonObject params) {
        try {

            logger.info("Processando requisição para reservar_ticket");

            // Acessa os parâmetros diretamente pelas suas chaves
            String cpf = params.get("cpf").getAsString();
            String data = params.get("data").getAsString();
            String hora = params.get("hora").getAsString();
            String origem = params.get("origem").getAsString();
            String destino = params.get("destino").getAsString();
            String nome = params.get("nome").getAsString();
            int poltrona = params.get("poltrona").getAsInt();

            // Chama o método de reserva
            return passagem.reservar_ticket(cpf, data, hora, origem, destino, nome, poltrona);
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }


    public String atualizar_reserva(JsonObject params) {
        try {

            logger.info("Procesando requisição para atualizar_reserva");

            String ticketId = params.get("ticketId").getAsString();

            if(!passagem.reservaExiste(ticketId)){
                logger.severe("Erro: ticket não encontrado ou cancelado.");
                return "Erro: Reserva não encontrada ou já cancelada.";
            }


            String cpf = params.get("cpf").getAsString();
            String data = params.get("data").getAsString();
            String hora = params.get("hora").getAsString();
            String origem = params.get("origem").getAsString();
            String destino = params.get("destino").getAsString();
            String nome = params.get("nome").getAsString();
            int poltrona = params.get("poltrona").getAsInt();

            Ticket atualizacao = new Ticket(cpf, data, hora, origem, destino, nome, poltrona);
            String at =  passagem.atualizar_reserva(ticketId, atualizacao);
            return "Reserva atualizada! ID: " + at;
        } catch (Exception e) {
            logger.severe("Erro ao processar atualizar_reserva: " + e.getMessage());
            return "Erro: " + e.getMessage();
        }
    }

    public String cancelar_reserva(JsonObject params) {

        try {
            logger.info("Processando requisição para cancelar_reserva");

            String ticketId = params.get("ticketId").getAsString();
            return passagem.cancelar_reserva(ticketId);
        } catch (Exception e) {
            logger.severe("Erro ao processar cancelar_reserva: " + e.getMessage());
            return "Erro: " + e.getMessage();
        }
    }

    public String consultar_reserva(JsonObject params) {
        try {
            logger.info("Processando requisição para consultar_reserva");

            String cpf = params.get("cpf").getAsString();
            List<String> reservas = passagem.consultar_reserva(cpf);
            return String.join(";", reservas);
        } catch (Exception e) {
            logger.severe("Erro ao processar consultar_reserva: " + e.getMessage());
            return "Erro: " + e.getMessage();
        }
    }
}
