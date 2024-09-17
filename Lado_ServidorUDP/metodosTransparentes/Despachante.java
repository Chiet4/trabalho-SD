package com.anchietaalbano.trabalho.metodosTransparentes;

import com.anchietaalbano.trabalho.Message;
import com.anchietaalbano.trabalho.Resposta;
import com.anchietaalbano.trabalho.logs.LoggerColorido;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.logging.Logger;

public class Despachante {
    private final Esqueleto esqueleto;
    private static final Gson gson = new Gson();

    public Despachante() {
        this.esqueleto = new Esqueleto();

    }

    public String invoke(Message message) {
        try {
            LoggerColorido.logInfo("Recebendo requisição: " + message);

            String metodo = message.getMethodId();
            JsonObject params = message.getParams();

            Resposta resposta; // Usaremos a classe Resposta agora

            switch (metodo) {
                case "reservar_ticket":
                    LoggerColorido.logInfo("Invocando método: reservar_ticket");
                    resposta = esqueleto.reservar_ticket(params);
                    break;
                case "atualizar_reserva":
                    LoggerColorido.logInfo("Invocando método: atualizar_ticket");
                    resposta = esqueleto.atualizar_reserva(params);
                    break;
                case "cancelar_reserva":
                    LoggerColorido.logInfo("Invocando método: cancelar_reserva");
                    resposta = esqueleto.cancelar_reserva(params);
                    break;
                case "consultar_reserva":
                    LoggerColorido.logInfo("Invocando método: consultar_reserva");
                    resposta = esqueleto.consultar_reserva(params);
                    break;
                case "consultar_historico":
                    LoggerColorido.logInfo("Invocando método: consultar_historico");
                    resposta = esqueleto.consultar_historico();
                    break;
                default:
                    LoggerColorido.logErro("Método não encontrado: " + metodo);
                    resposta = Resposta.notFound("Erro: Método não encontrado");
                    break;
            }

            // Construção da resposta JSON
            JsonObject arguments = new JsonObject();
            arguments.addProperty("result", resposta.getMensagem());
            arguments.addProperty("status", resposta.getCodigo());

            // Criação do objeto de resposta Message
            Message responseMessage = new Message();
            responseMessage.setMessageType(1); // Tipo de resposta
            responseMessage.setRequestId(message.getRequestId());
            responseMessage.setMethodId(metodo);
            responseMessage.setArguments(arguments);

            // Conversão do objeto Message para JSON
            String jsonResponse = gson.toJson(responseMessage);
            LoggerColorido.logInfo("Resposta gerada: " + jsonResponse);

            return jsonResponse;

        } catch (Exception e) {
            LoggerColorido.logInfo("Erro ao processar a requisição:  " + e.getMessage());
            return "Erro: " + e.getMessage();
        }
    }
}
