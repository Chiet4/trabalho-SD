package com.anchietaalbano.trabalho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.anchietaalbano.trabalho.Ticket.idManager;

public class Passagem {
    private final Map<String, Ticket> tickets = new HashMap<>();


    public String reservar_ticket(String cpf, String data, String hora, String origem, String destino, String nome, int poltrona) throws Exception {

        for (Ticket t : tickets.values()) {
            if (t.getPoltrona() == poltrona) {
                throw new Exception("Poltrona já reservada! Escolha outra!");
            }
        }

        if (tickets.size() >= 50) { // Supondo que a capacidade do ônibus seja 50
            throw new Exception("Ônibus cheio");
        }

        // Cria e adiciona o ticket ao mapa
        Ticket novoTicket = new Ticket(cpf, data, hora, origem, destino, nome, poltrona);
        tickets.put(novoTicket.getId(), novoTicket);

        return novoTicket.getId();
    }

    public String atualizar_reserva(String ticketId, Ticket atualizacao) throws Exception {
        Ticket ticket = tickets.get(ticketId);

        if (ticket == null) {
            throw new Exception("Ticket não existe!");
        }

        idManager.liberarId(ticketId);
        // Remove o ticket antigo
        tickets.remove(ticketId);

        tickets.put(atualizacao.getId(), atualizacao);

        return atualizacao.getId();
    }

    public String cancelar_reserva(String ticketId) throws Exception {
        Ticket ticketRemove = tickets.get(ticketId);

        if (ticketRemove == null) {
            throw new Exception("Ticket não existe!");
        }

        tickets.remove(ticketId);
        Ticket.idManager.liberarId(ticketId);

        return "Ticket cancelado: " + ticketId;
    }

    public List<String> consultar_reserva(String cpf) throws Exception {
        List<String> resultado = new ArrayList<>();

        for (Ticket t : tickets.values()) {
            if (t.getCpf().equals(cpf)) {
                resultado.add(t.toString());
            }
        }

        if (resultado.isEmpty()) {
            throw new Exception("Não há tickets reservados para esse CPF.");
        }

        return resultado;
    }

    public List<String> consultar_historico(){
        List<String> resultado = new ArrayList<>();

        for (Ticket t : tickets.values()) {
            resultado.add(t.toString());
        }

        return resultado;
    }

    public boolean reservaExiste(String ticketId) {
        Ticket ticket = tickets.get(ticketId);
        return ticket != null;
    }

}
