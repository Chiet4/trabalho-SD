package com.anchietaalbano.trabalho;

import com.google.gson.JsonObject;

import java.util.List;
import java.util.logging.Logger;

public class Esqueleto {
    private final Passagem passagem;
    private static final Logger logger = Logger.getLogger(Esqueleto.class.getName());

    public Esqueleto() {
        this.passagem = new Passagem();
    }

    public Resposta reservar_ticket(JsonObject params) {
        try {
            logger.info("Processando requisição para reservar_ticket");

            if (!ValidacaoDeDados.validarTicket(params)) {
                return Resposta.badRequest("Erro: Dados inválidos na requisição.");
            }

            String cpf = params.get("cpf").getAsString();
            String data = params.get("data").getAsString();
            String hora = params.get("hora").getAsString();
            String origem = params.get("origem").getAsString();
            String destino = params.get("destino").getAsString();
            String nome = params.get("nome").getAsString();
            int poltrona = params.get("poltrona").getAsInt();

            String resultado = passagem.reservar_ticket(cpf, data, hora, origem, destino, nome, poltrona);
            return Resposta.criado("Reserva criada com sucesso: " + resultado);
        } catch (Exception e) {
            logger.severe("Erro interno ao reservar ticket: " + e.getMessage());
            return Resposta.erroInterno("Erro interno ao reservar ticket.");
        }
    }

    public Resposta atualizar_reserva(JsonObject params) {
        try {
            logger.info("Processando requisição para atualizar_reserva");

            String ticketId = params.get("ticketId").getAsString();

            if (!passagem.reservaExiste(ticketId)) {
                return Resposta.notFound("Erro: Reserva não encontrada ou já cancelada.");
            }

            if (!ValidacaoDeDados.validarTicket(params)) {
                return Resposta.badRequest("Erro: Dados inválidos na requisição.");
            }

            String cpf = params.get("cpf").getAsString();
            String data = params.get("data").getAsString();
            String hora = params.get("hora").getAsString();
            String origem = params.get("origem").getAsString();
            String destino = params.get("destino").getAsString();
            String nome = params.get("nome").getAsString();
            int poltrona = params.get("poltrona").getAsInt();

            String resultado = passagem.atualizar_reserva(ticketId, new Ticket(cpf, data, hora, origem, destino, nome, poltrona));
            return Resposta.ok("Reserva atualizada com sucesso: " + resultado);
        } catch (Exception e) {
            logger.severe("Erro interno ao atualizar reserva: " + e.getMessage());
            return Resposta.erroInterno("Erro interno ao atualizar reserva.");
        }
    }

    public Resposta cancelar_reserva(JsonObject params) {
        try {
            logger.info("Processando requisição para cancelar_reserva");

            String ticketId = params.get("ticketId").getAsString();

            if (!ValidacaoDeDados.validarCancelarReserva(params)) {
                return Resposta.badRequest("Erro: Dados inválidos na requisição.");
            }

            String resultado = passagem.cancelar_reserva(ticketId);
            return Resposta.ok("Reserva cancelada com sucesso: " + resultado);
        } catch (Exception e) {
            logger.severe("Erro interno ao cancelar reserva: " + e.getMessage());
            return Resposta.erroInterno("Erro interno ao cancelar reserva.");
        }
    }

    public Resposta consultar_reserva(JsonObject params) {
        try {
            logger.info("Processando requisição para consultar_reserva");

            String cpf = params.get("cpf").getAsString();

            if (!ValidacaoDeDados.validarConsultarReserva(params)) {
                return Resposta.badRequest("Erro: Dados inválidos na requisição.");
            }

            List<String> reservas = passagem.consultar_reserva(cpf);
            if (reservas.isEmpty()) {
                return Resposta.notFound("Nenhuma reserva encontrada para o CPF informado.");
            }

            return Resposta.ok(String.join(";", reservas));
        } catch (Exception e) {
            logger.severe("Erro interno ao consultar reserva: " + e.getMessage());
            return Resposta.erroInterno("Erro interno ao consultar reserva.");
        }
    }

    public Resposta consultar_historico() {
        try {
            logger.info("Processando requisição para consultar_historico");

            List<String> historicos = passagem.consultar_historico();
            return Resposta.ok(String.join(";", historicos));
        } catch (Exception e) {
            logger.severe("Erro interno ao consultar histórico: " + e.getMessage());
            return Resposta.erroInterno("Erro interno ao consultar histórico.");
        }
    }
}
