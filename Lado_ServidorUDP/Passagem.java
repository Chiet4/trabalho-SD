package com.anchietaalbano.trabalho;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;

import java.util.*;

public class Passagem {
    private final Map<String, Ticket> tickets = new HashMap<>();
    private final Set<Integer> poltronasReservadas = new HashSet<>();
    private static final int CAPACIDADE_ONIBUS = 50;

    public String reservar_ticket(String cpf, String data, String hora, String origem, String destino, String nome, int poltrona) throws Exception {
        if (poltronasReservadas.contains(poltrona)) {
            throw new Exception("Poltrona já reservada! Escolha outra!");
        }

        if (tickets.size() >= CAPACIDADE_ONIBUS) {
            throw new Exception("Ônibus cheio");
        }

        // Cria e adiciona o ticket ao mapa
        Ticket novoTicket = new Ticket(cpf, data, hora, origem, destino, nome, poltrona);
        tickets.put(novoTicket.getId(), novoTicket);
        poltronasReservadas.add(poltrona);

        return novoTicket.getId();
    }

    public String atualizar_reserva(String ticketId, Ticket atualizacao) throws Exception {
        Ticket ticket = tickets.get(ticketId);

        if (ticket == null) {
            throw new Exception("Ticket não existe!");
        }

        // Remove a poltrona antiga e libera o ID
        poltronasReservadas.remove(ticket.getPoltrona());
        tickets.remove(ticketId);
        Ticket.idManager.liberarId(ticketId);

        // Adiciona o ticket atualizado
        tickets.put(atualizacao.getId(), atualizacao);
        poltronasReservadas.add(atualizacao.getPoltrona());

        return atualizacao.getId();
    }

    public String cancelar_reserva(String ticketId) throws Exception {
        Ticket ticketRemove = tickets.get(ticketId);

        if (ticketRemove == null) {
            throw new Exception("Ticket não existe!");
        }

        tickets.remove(ticketId);
        poltronasReservadas.remove(ticketRemove.getPoltrona());
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

    public List<String> consultar_historico() {
        List<String> resultado = new ArrayList<>();

        for (Ticket t : tickets.values()) {
            resultado.add(t.toString());
        }

        return resultado;
    }

    public boolean reservaExiste(String ticketId) {
        return tickets.containsKey(ticketId);
    }
}


