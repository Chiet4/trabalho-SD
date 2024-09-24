package com.anchietaalbano.trabalho.logs;

public class LoggerColorido {

    // Códigos de cores ANSI
    public static final String RESET = "\u001B[0m";
    public static final String VERMELHO = "\u001B[31m";
    public static final String VERDE = "\u001B[32m";
    public static final String AMARELO = "\u001B[33m";
    public static final String AZUL = "\u001B[34m";

    public static void logInfo(String mensagem) {
        System.out.println(VERDE + "INFO: " + mensagem + RESET);
    }

    public static void logErro(String mensagem) {
        System.out.println(VERMELHO + "ERRO: " + mensagem + RESET);
    }

    public static void logAviso(String mensagem) {
        System.out.println(AMARELO + "AVISO: " + mensagem + RESET);
    }

}