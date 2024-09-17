package com.anchietaalbano.trabalho.exceptions;

import java.io.IOException;
import java.net.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class NetworkExceptionHandler {
    private static final Logger logger = Logger.getLogger(NetworkExceptionHandler.class.getName());
    private static final int PORT_ALTERNATIVA = 9877;  // Porta de fallback para BindException
    private static DatagramSocket serverSocket;


    public static void handleSocketException(SocketException e) {
        if (e.getMessage().contains("Address already in use")) {
            logger.log(Level.SEVERE, "Porta já em uso. Tentando usar porta alternativa.");
            try {
                serverSocket = new DatagramSocket(PORT_ALTERNATIVA);
                logger.log(Level.INFO, "Porta alternativa " + PORT_ALTERNATIVA + " utilizada com sucesso.");
            } catch (SocketException ex) {
                logger.log(Level.SEVERE, "Falha ao usar porta alternativa: ", ex);

                System.exit(1);
            }
        } else {
            logger.log(Level.SEVERE, "Erro de socket desconhecido: ", e);

            System.exit(1);
        }
    }


    public static void handleBindException(BindException e) {
        logger.log(Level.SEVERE, "Erro ao associar o socket à porta: ", e);

        try {
            logger.info("Aguardando 5 segundos antes de tentar novamente...");
            Thread.sleep(5000);

            serverSocket = new DatagramSocket(PORT_ALTERNATIVA);
            logger.info("Conexão bem-sucedida usando porta alternativa: " + PORT_ALTERNATIVA);
        } catch (InterruptedException | SocketException ex) {
            logger.log(Level.SEVERE, "Falha ao reassociar o socket: ", ex);

            System.exit(1);
        }
    }

    // Tratamento para PortUnreachableException
    public static void handlePortUnreachableException(PortUnreachableException e) {
        logger.log(Level.WARNING, "Porta de destino inalcançável: ", e);
        // Tentar reenviar o pacote ou apenas logar o problema
        // Reenviar pacote poderia ser feito aqui se desejado
        logger.info("Tentativa de reenviar pacotes falhou. Notificando cliente.");
        // Aqui pode-se criar uma lógica de envio de resposta para o cliente
    }

    // Tratamento para UnknownHostException (host desconhecido)
    public static void handleUnknownHostException(UnknownHostException e) {
        logger.log(Level.SEVERE, "Host desconhecido: ", e);
        // Notificar o cliente ou logar o problema
        logger.info("Falha ao resolver o host. Verifique o endereço fornecido.");
        // Se possível, solicitar novo endereço ao cliente
    }

    // Tratamento para NoRouteToHostException (sem rota para o host)
    public static void handleNoRouteToHostException(NoRouteToHostException e) {
        logger.log(Level.SEVERE, "Sem rota para o host de destino: ", e);
        // Esperar e tentar reenviar pacotes
        try {
            logger.info("Tentando novamente em 5 segundos...");
            Thread.sleep(5000);
            // Aqui poderia ser implementada a lógica de tentar se reconectar ou reenviar pacotes
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "Thread foi interrompida durante a tentativa de reconexão: ", ex);
        }
    }

    // Tratamento para IOException genérica
    public static void handleIOException(IOException e) {
        logger.log(Level.SEVERE, "Erro de I/O durante o envio ou recebimento de pacotes: ", e);
        // Tentar reconectar ou reenviar o pacote
        try {
            logger.info("Tentando reconectar em 5 segundos...");
            Thread.sleep(5000);
            // Aqui você poderia tentar reenviar o pacote ou reiniciar a conexão
        } catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            logger.log(Level.SEVERE, "Falha durante a tentativa de reconexão: ", ex);
        }
    }

    // Tratamento para SecurityException (problema de permissões)
    public static void handleSecurityException(SecurityException e) {
        logger.log(Level.SEVERE, "Erro de segurança ao tentar abrir o socket: ", e);
        // Avisar o administrador sobre permissões
        logger.info("Verifique as permissões de segurança do sistema. O servidor será encerrado.");
        // Encerrar o servidor por falta de permissões
        System.exit(1);
    }
}
