package com.anchietaalbano.trabalho;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Ticket {
    private String id;
    private String cpf;
    private String data;
    private String hora;
    private String origem;
    private String destino;
    private String nome;
    private int poltrona;
    private static final IdManager idManager = new IdManager();

    // Construtor
    public Ticket(String cpf, String data, String hora, String origem, String destino, String nome, int poltrona) {
        this.cpf = cpf;
        this.data = data;
        this.hora = hora;
        this.origem = origem;
        this.destino = destino;
        this.nome = nome;
        this.poltrona = poltrona;
        this.id = idManager.gerarId();
    }

    private String generateId() {
        return "TICKET-" + poltrona + "-" + cpf.substring(cpf.length() - 3) + "-" + data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getPoltrona() {
        return poltrona;
    }

    public void setPoltrona(int poltrona) {
        this.poltrona = poltrona;
    }


    @Override
    public String toString() {
        String s = "";
        String poltro = String.valueOf(poltrona);
        s = "Ticket [" + getId() + ", " + nome + ", " + cpf + ", "
                + data + ", " + hora + ", " + origem
                + ", " + destino + ", " + poltro + "]";

        return s;
    }
}


class IdManager {
    private final List<String> availableIds;
    private final List<String> usedIds;
    private final Random random;

    public IdManager() {
        this.availableIds = new ArrayList<>();
        this.usedIds = new ArrayList<>();
        this.random = new Random();
        GenerateIds();
    }

    private void GenerateIds() {
        // Inicializa os IDs disponíveis (exemplo com 10000 IDs)
        for (int i = 1; i <= 10000; i++) {
            availableIds.add(String.format("%04d", i));
        }
    }

    public String gerarId() {
        if (availableIds.isEmpty()) {
            throw new RuntimeException("No available IDs left");
        }
        int index = random.nextInt(availableIds.size());
        String id = availableIds.remove(index);
        usedIds.add(id);
        return id;
    }

    public void liberarId(String id) {
        if (usedIds.remove(id)) {
            availableIds.add(id);
        }
    }
}

