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
        esqueleto = new Esqueleto();
    }

    public String invoke(Message message) {
        try {
            String methodName = message.getMethodId();
            JsonObject params = message.getParams();


            Method methodToInvoke = encontrarMetodo(methodName);
            if (methodToInvoke == null) {
                return construirRespostaErro("Erro: Método não encontrado: " + methodName);
            }

            Resposta resposta = (Resposta) methodToInvoke.invoke(esqueleto, params);
            return construirMensagemResposta(message.getRequestId(), methodToInvoke.getName(), resposta);

        } catch (ReflectiveOperationException e) {
            LoggerColorido.logErro("Erro de reflexão: " + e.getMessage());
            return construirRespostaErro("Erro de reflexão: " + e.getMessage());
        } catch (Exception e) {
            LoggerColorido.logErro("Erro ao processar a requisição: " + e.getMessage());
            return construirRespostaErro("Erro ao processar a requisição: " + e.getMessage());
        }
    }

    private Method encontrarMetodo(String methodName) {
        try {
            return Esqueleto.class.getDeclaredMethod(methodName, JsonObject.class);
        } catch (NoSuchMethodException e) {
            return null; // Método não encontrado
        }
    }

    private String construirRespostaErro(String mensagemErro) {
        Resposta resposta = Resposta.badRequest(mensagemErro);
        return construirMensagemResposta(-1, null, resposta); // -1 para requestId não aplicável em erros
    }

    private String construirMensagemResposta(int requestId, String methodName, Resposta resposta) {
        JsonObject arguments = new JsonObject();
        arguments.addProperty("result", resposta.getMensagem());
        arguments.addProperty("status", resposta.getCodigo());

        Message responseMessage = new Message();
        responseMessage.setMessageType(1); // Tipo de resposta
        responseMessage.setRequestId(requestId);
        responseMessage.setMethodId(methodName);
        responseMessage.setArguments(arguments);

        // Conversão do objeto Message para JSON
        String jsonResponse = gson.toJson(responseMessage);
        LoggerColorido.logInfo("Resposta gerada: " + jsonResponse);
        return jsonResponse;
    }
}

