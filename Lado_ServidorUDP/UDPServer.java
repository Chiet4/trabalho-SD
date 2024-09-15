package com.anchietaalbano.trabalho;

import com.google.gson.Gson;


import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import java.util.logging.Logger;

public class UDPServer {
    private static final int PORT = 9876;
    private static final Gson gson = new Gson();
    private static final Despachante despachante = new Despachante();
    private static final Logger logger = Logger.getLogger(UDPServer.class.getName());
    private static final Map<String, String> HistoricoRequest = new HashMap<>();
    private static DatagramSocket serverSocket = null;

    public static void main(String[] args) {

        try {
            serverSocket = new DatagramSocket(PORT);
            logger.info("Servidor iniciado!");
            byte[] receiveData = new byte[4096];

            do {
                DatagramPacket receivePacket = getRequest(serverSocket, receiveData);

                // Processa a requisição e gera a resposta
                String response = processRequest(receivePacket);

                // Envia a resposta de volta ao cliente
                sendReply(serverSocket, response, receivePacket.getAddress(), receivePacket.getPort());
            } while (true);
        } catch (BindException e) {
            NetworkExceptionHandler.handleBindException(e);
        } catch (PortUnreachableException e) {
            NetworkExceptionHandler.handlePortUnreachableException(e);
        } catch (NoRouteToHostException e) {
            NetworkExceptionHandler.handleNoRouteToHostException(e);
        } catch (SocketException e) {
            NetworkExceptionHandler.handleSocketException(e);
        } catch (UnknownHostException e) {
            NetworkExceptionHandler.handleUnknownHostException(e);
        } catch (IOException e) {
            NetworkExceptionHandler.handleIOException(e);
        } catch (SecurityException e) {
            NetworkExceptionHandler.handleSecurityException(e);
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close(); // Fechar o socket corretamente
                logger.info("Socket fechado com sucesso.");
            }
        }
    }


    // Recebimento do Datagrama UDP do cliente
    private static DatagramPacket getRequest(DatagramSocket serverSocket, byte[] receiveData) throws IOException {
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        logger.info("Received packet from " + receivePacket.getAddress() + ":" + receivePacket.getPort());

        return receivePacket;
    }

    // Envio do Datagrama/respota do serverUDP para o clienteUDP
    private static void sendReply(DatagramSocket serverSocket, String response, InetAddress clientAddress, int clientPort) throws IOException {
        byte[] sendData = response.getBytes(StandardCharsets.UTF_8);

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
        serverSocket.send(sendPacket);
        logger.info("Sent response to " + clientAddress + ":" + clientPort + " = " + Arrays.toString(sendData));
    }

    private static String processRequest(DatagramPacket receivePacket) {

        String request = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength(), StandardCharsets.UTF_8);

        logger.info("Requisição recebida: " + request);

        Message message = new Message(request);
        String requestId = message.getMethodId();


        if(HistoricoRequest.containsKey(requestId)) {
            logger.info("Request duplicada detectada. Retornando resposta em historio.");
            return HistoricoRequest.get(requestId);
        }

        String response = despachante.invoke(message);

        HistoricoRequest.put(requestId, response);

        return response;

    }

}

