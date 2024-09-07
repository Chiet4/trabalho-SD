package com.anchietaalbano.trabalho;

import java.util.List;

public class Esqueleto {
    private Passagem passagem;

    public Esqueleto() {
        this.passagem = new Passagem(); // Instancia a classe Passagem
    }

    public String reservar_ticket(String args) {
        try {
            String[] params = args.split(",");
            if (params.length != 7) throw new IllegalArgumentException("Número de parâmetros inválido.");

            String cpf = params[0];
            String data = params[1];
            String hora = params[2];
            String origem = params[3];
            String destino = params[4];
            String nome = params[5];
            int poltrona = Integer.parseInt(params[6]);

            return passagem.reservar_ticket(cpf, data, hora, origem, destino, nome, poltrona);
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    public String atualizar_reserva(String args) {
        try {
            String[] params = args.split(",");
            if (params.length != 8) throw new IllegalArgumentException("Número de parâmetros inválido.");

            String ticketId = params[0];
            String cpf = params[1];
            String data = params[2];
            String hora = params[3];
            String origem = params[4];
            String destino = params[5];
            String nome = params[6];
            int poltrona = Integer.parseInt(params[7]);

            Ticket atualizacao = new Ticket(cpf, data, hora, origem, destino, nome, poltrona);
            passagem.atualizar_reserva(ticketId, atualizacao);
            return "Reserva atualizada!";
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    public String cancelar_reserva(String args) {
        //System.out.println(args.split(",").length);
        try {
            return passagem.cancelar_reserva(args);
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }

    public String consultar_reserva(String args) {
        try {
            List<String> reservas = passagem.consultar_reserva(args);
            return String.join(";", reservas);
        } catch (Exception e) {
            return "Erro: " + e.getMessage();
        }
    }
}
