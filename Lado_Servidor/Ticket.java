package com.anchietaalbano.trabalho;

public class Ticket {
    private String id;
    private String cpf;
    private String data;
    private String hora;
    private String origem;
    private String destino;
    private String nome;
    private int poltrona;

    // Construtor
    public Ticket(String cpf, String data, String hora, String origem, String destino, String nome, int poltrona) {
        this.cpf = cpf;
        this.data = data;
        this.hora = hora;
        this.origem = origem;
        this.destino = destino;
        this.nome = nome;
        this.poltrona = poltrona;
        this.id = generateId(); // Método para gerar um ID único para o ticket
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

    private String generateId() {
        return "TICKET-" + poltrona + "-" + cpf.substring(cpf.length() - 3) + "-" + data;
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

