package com.anchietaalbano.trabalho;

public class Resposta {
    private final int codigo;
    private final String mensagem;

    public Resposta(int codigo, String mensagem) {
        this.codigo = codigo;
        this.mensagem = mensagem;
    }

    public int getCodigo() {
        return codigo;
    }

    public String getMensagem() {
        return mensagem;
    }


    public static Resposta ok(String mensagem) {
        return new Resposta(200, mensagem);
    }

    public static Resposta criado(String mensagem) {
        return new Resposta(201, mensagem);
    }

    public static Resposta badRequest(String mensagem) {
        return new Resposta(400, mensagem);
    }

    public static Resposta notFound(String mensagem) {
        return new Resposta(404, mensagem);
    }

    public static Resposta erroInterno(String mensagem) {
        return new Resposta(500, mensagem);
    }

}
