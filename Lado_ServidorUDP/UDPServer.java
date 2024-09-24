package com.anchietaalbano.trabalho;

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
    private static final int MAX_REQUESTS_BEFORE_FAILURE = 3; // Defina o limite desejado (por exemplo, 2 ou 3)
    private static int requestCounter = 0;

    private static final Map<String, String> HistoricoRequest = new HashMap<>();
    private static DatagramSocket serverSocket = null;


    public static void main(String[] args) {

        try {
            serverSocket = new DatagramSocket(PORT);

            LoggerColorido.logInfo("Servidor iniciado!");

            while (true) {
                DatagramPacket receivePacket = getRequest();

                if (qt_time()) {
                    LoggerColorido.logAviso("Simulando falha: processando a requisição, mas não enviando resposta.");
                    processRequest(receivePacket); // Processa a requisição, mas não envia resposta
                    continue;
                }

                // Processa a requisição e gera a resposta
                String response = processRequest(receivePacket);


                //testes
                //serverSocket.close();
                //InetAddress invalidAddress = InetAddress.getByName("invalido.local"); //UnknownHost

                // Envia a resposta de volta ao cliente
                sendReply(serverSocket, response, receivePacket.getAddress(), receivePacket.getPort());
            }
        } catch (BindException e) {
            LoggerColorido.logErro("Porta já está em uso: " + e.getMessage());
        } catch (PortUnreachableException e) {
            LoggerColorido.logErro("Porta do cliente inacessível: " + e.getMessage());
        } catch (SocketException e) {
            LoggerColorido.logErro("Erro de socket: " + e.getMessage());
        } catch (UnknownHostException e) {
            LoggerColorido.logErro("Host desconhecido: " + e.getMessage());
        } catch (IOException e) {
            LoggerColorido.logErro("Erro de I/O: " + e.getMessage());
        } catch (Exception e) {
            LoggerColorido.logErro("Erro inesperado: " + e.getMessage());
        } finally {
            if (serverSocket != null && !serverSocket.isClosed()) {
                serverSocket.close();
                LoggerColorido.logInfo("Servidor fechado!");
            }
        }
    }

    private static boolean qt_time() {
        requestCounter++;
        if (requestCounter == MAX_REQUESTS_BEFORE_FAILURE) {
            requestCounter = 0;  // Reseta o contador
            return true;  // Simula a falha
        }
        return false;  // Continua normalmente
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


        if (HistoricoRequest.containsKey(requestHash)) {
            LoggerColorido.logErro("Request duplicada detectada. Retornando resposta do histórico.");
            return HistoricoRequest.get(requestHash);
        }

        String response = despachante.invoke(message);
        if (response != null && !response.isEmpty()) {
            HistoricoRequest.put(requestHash, response);
        }
        return response;

    }

    private static String gerarSimplesHash(Message message){
        String uniqueMessage = message.getMethodId() + message.getMessageType() + message.getMethodId() + message.getParams().toString();
        return Integer.toString(uniqueMessage.hashCode());
    }

}

