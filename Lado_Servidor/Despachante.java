package com.anchietaalbano.trabalho;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

public class Despachante {
    private Esqueleto esqueleto;
    private static final Gson gson = new Gson();
    private static final Logger logger = Logger.getLogger(Despachante.class.getName());

    public Despachante() {
        this.esqueleto = new Esqueleto();
    }

    public String invoke(String request) {

        logger.info("Recebendo requisição: "+request);
        JsonObject json = JsonParser.parseString(request).getAsJsonObject();

        String metodo = json.get("methodId").getAsString();


        JsonObject params = json.get("arguments").getAsJsonObject();

        // Converte o objeto JSON decodificado em uma string de argumentos separados por vírgula
        StringBuilder argsBuilder = new StringBuilder();
        for (String key : params.keySet()) {
            if (!argsBuilder.isEmpty()) {
                argsBuilder.append(",");
            }
            argsBuilder.append(key).append(",").append(params.get(key).getAsString());
        }

        String args = argsBuilder.toString();

        logger.info("Argumentos extraídos: "+ args);

        String result;
        int status;
        switch (metodo) {
            case "reservar_ticket":
                result = esqueleto.reservar_ticket(args);
                status = 201; // Created
                break;
            case "atualizar_reserva":
                result = esqueleto.atualizar_reserva(args);
                status = 200; // OK
                break;
            case "cancelar_reserva":
                result = esqueleto.cancelar_reserva(args);
                status = 200; // OK
                break;
            case "consultar_reserva":
                result = esqueleto.consultar_reserva(args);
                status = 200; // OK
                break;
            default:
                result = "Erro: Método não encontrado";
                status = 404; // Not Found
                logger.severe("Método não encontrado: " + metodo);
                break;
        }

        JsonObject response = new JsonObject();
        response.addProperty("messageType", 1);
        response.addProperty("requestId", json.get("requestId").getAsInt());
        response.addProperty("methodId", metodo);
        response.addProperty("status", status);


        JsonArray responseArray = new JsonArray();

        responseArray.add(result);

        response.add("arguments", responseArray);

        logger.info("Resposta: "+response);

        return gson.toJson(response);
    }
}
