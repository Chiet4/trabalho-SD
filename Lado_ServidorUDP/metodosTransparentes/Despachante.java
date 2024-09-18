package com.anchietaalbano.trabalho.metodosTransparentes;

import com.anchietaalbano.trabalho.Message;
import com.anchietaalbano.trabalho.Resposta;
import com.anchietaalbano.trabalho.logs.LoggerColorido;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.lang.reflect.Method;

public class Despachante {
    private final Esqueleto esqueleto;
    private static final Gson gson = new Gson();

    public Despachante() {
        this.esqueleto = new Esqueleto();
    }

    public String invoke(Message message) {
        try {
            LoggerColorido.logInfo("Recebendo requisição: " + message);

            String methodName = message.getMethodId();
            JsonObject params = message.getParams();

            // Obter o método correspondente no objeto Esqueleto usando reflexão
            Method[] methods = Esqueleto.class.getDeclaredMethods();
            Method methodToInvoke = null;
            for (Method method : methods) {
                if (method.getName().equals(methodName)) {
                    methodToInvoke = method;
                    break;
                }
            }

            if (methodToInvoke == null) {
                LoggerColorido.logErro("Método não encontrado: " + methodName);
                return "Erro: Método não encontrado";
            }

            Resposta resposta = (Resposta) methodToInvoke.invoke(esqueleto, params);

            // Construção da resposta JSON
            JsonObject arguments = new JsonObject();
            arguments.addProperty("result", resposta.getMensagem());
            arguments.addProperty("status", resposta.getCodigo());

            Message responseMessage = new Message();
            responseMessage.setMessageType(1); // Tipo de resposta
            responseMessage.setRequestId(message.getRequestId());
            responseMessage.setMethodId(methodToInvoke.getName());
            responseMessage.setArguments(arguments);

            // Conversão do objeto Message para JSON
            String jsonResponse = gson.toJson(responseMessage);
            LoggerColorido.logInfo("Resposta gerada: " + jsonResponse);

            return jsonResponse;
        } catch (Exception e) {
            LoggerColorido.logInfo("Erro ao processar a requisição: " + e.getMessage());
            return "Erro: " + e.getMessage();
        }
    }
}
