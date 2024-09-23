package com.anchietaalbano.trabalho;

import com.anchietaalbano.trabalho.exceptions.NetworkExceptionHandler;
import com.anchietaalbano.trabalho.logs.LoggerColorido;
import com.anchietaalbano.trabalho.metodosTransparentes.Despachante;

import java.io.IOException;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



public class UDPServer {
    private static final int PORT = 9876;
    private static final Despachante despachante = new Despachante();

    private static final Map<String, String> HistoricoRequest = new HashMap<>();
    private static DatagramSocket serverSocket = null;

    public static void main(String[] args) {

        try {
            serverSocket = new DatagramSocket(PORT);
            LoggerColorido.logInfo("Servidor iniciado!");
            byte[] receiveData = new byte[512];

            while (true) {
                DatagramPacket receivePacket = getRequest();

                // Processa a requisição e gera a resposta
                String response = processRequest(receivePacket);

                // Envia a resposta de volta ao cliente
                sendReply(serverSocket, response, receivePacket.getAddress(), receivePacket.getPort());
            }
        } catch (Exception e) {
            handleException(e);
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                LoggerColorido.logInfo("Servidor fechado!");
            }
        }
    }
    
    // Recebimento do Datagrama UDP do cliente
    private static DatagramPacket getRequest() throws IOException {
        byte[] receiveData = new byte[512];  // Define o buffer de recebimento dentro do método
        DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
        serverSocket.receive(receivePacket);
        LoggerColorido.logInfo("Received packet from " + receivePacket.getAddress() + ":" + receivePacket.getPort());

        return receivePacket;
    }

    // Envio do Datagrama/respota do serverUDP para o clienteUDP
    private static void sendReply(DatagramSocket serverSocket, String response, InetAddress clientAddress, int clientPort) throws IOException {
        byte[] sendData = response.getBytes(StandardCharsets.UTF_8);

        DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, clientAddress, clientPort);
        serverSocket.send(sendPacket);
        LoggerColorido.logInfo("Sent response to " + clientAddress + ":" + clientPort + " = " + Arrays.toString(sendData));
    }

    private static String processRequest(DatagramPacket receivePacket) {

        String request = new String(receivePacket.getData(), receivePacket.getOffset(), receivePacket.getLength(), StandardCharsets.UTF_8);

        LoggerColorido.logInfo("Received request:" + request);

        Message message = new Message(request);

        String requestHash = gerarSimplesHash(message);

        if(HistoricoRequest.containsKey(requestHash)) {
            LoggerColorido.logInfo("Request duplicada detectada. Retornando resposta em historio.");
            return HistoricoRequest.get(requestHash);
        }

        String response = despachante.invoke(message);
        if (response != null && !response.isEmpty()) {
            HistoricoRequest.put(requestHash, response);
        }
        return response;

    }

    private static String gerarSimplesHash(Message message){
        String uniqueMessage = message.getRequestId() + message.getMethodId() + message.getParams().toString();
        return Integer.toString(uniqueMessage.hashCode());
    }


    private static void handleException(Exception e) {
        if (e instanceof BindException) {
            NetworkExceptionHandler.handleBindException((BindException) e);
        } else if (e instanceof PortUnreachableException) {
            NetworkExceptionHandler.handlePortUnreachableException((PortUnreachableException) e);
        } else if (e instanceof NoRouteToHostException) {
            NetworkExceptionHandler.handleNoRouteToHostException((NoRouteToHostException) e);
        } else if (e instanceof SocketException) {
            NetworkExceptionHandler.handleSocketException((SocketException) e);
        } else if (e instanceof UnknownHostException) {
            NetworkExceptionHandler.handleUnknownHostException((UnknownHostException) e);
        } else if (e instanceof IOException) {
            NetworkExceptionHandler.handleIOException((IOException) e);
        } else if (e instanceof SecurityException) {
            NetworkExceptionHandler.handleSecurityException((SecurityException) e);
        }
    }

}

