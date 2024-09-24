package com.anchietaalbano.trabalho.validates;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class ValidacaoDeDados {

    private static final Logger logger = Logger.getLogger(ValidacaoDeDados.class.getName());

    public static void validarTicket(JsonObject params) throws Exception {
        String cpf = getParam(params, "cpf");
        String nome = getParam(params, "nome");
        String data = getParam(params, "data");
        String hora = getParam(params, "hora");
        String origem = getParam(params, "origem");
        String destino = getParam(params, "destino");
        int poltrona = getIntParam(params);

        // Validações para cada argumento
        if (!validarCPF(cpf)) {
            throw new Exception("Validacao - CPF inválido: " + cpf);
        }
        if (!validarNome(nome)) {
            throw new Exception("Validacao - Nome inválido: " + nome);
        }
        if (!validarData(data)) {
            throw new Exception("Validacao - Data inválida: " + data);
        }
        if (!validarHora(hora)) {
            throw new Exception("Validacao - Hora inválida: " + hora);
        }
        if (!validarOrigemDestino(origem)) {
            throw new Exception("Validacao - Origem inválida: " + origem);
        }
        if (!validarOrigemDestino(destino)) {
            throw new Exception("Validacao - Destino inválido: " + destino);
        }
        if (!validarPoltrona(poltrona)) {
            throw new Exception("Validacao - Número da poltrona inválido: " + poltrona);
        }
    }

    public static boolean validarCPF(String cpf) {
        if ((cpf != null) && cpf.matches("\\d{11}")) {
            return true;
        }
        logger.warning("CPF inválido. Deve conter 11 dígitos numéricos.");
        return false;
    }

    private static boolean validarNome(String nome) {
        if (nome != null && nome.matches("[a-zA-Z\\s]+")) {
            return true;
        }
        logger.warning("Nome inválido. Deve conter apenas letras e espaços.");
        return false;
    }

    private static boolean validarData(String data) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        sdf.setLenient(false);
        try {
            sdf.parse(data);
            return true;
        } catch (ParseException e) {
            logger.warning("Data inválida. Use o formato YYYY-MM-DD.");
            return false;
        }
    }

    private static boolean validarHora(String hora) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        sdf.setLenient(false);
        try {
            sdf.parse(hora);
            return true;
        } catch (ParseException e) {
            logger.warning("Hora inválida. Use o formato HH:MM.");
            return false;
        }
    }

    private static boolean validarOrigemDestino(String local) {
        if (local != null && !local.trim().isEmpty()) {
            return true;
        }
        logger.warning("Origem/Destino não pode estar vazio.");
        return false;
    }

    private static boolean validarPoltrona(int poltrona) {
        if (poltrona >= 1 && poltrona <= 50) {
            return true;
        }
        logger.warning("Número da poltrona inválido. Deve estar entre " + 1 + " e " + 50 + ".");
        return false;
    }

    public static boolean validarCancelarReserva(JsonObject params) {
        return params.has("ticketId") && !params.get("ticketId").getAsString().isEmpty();
    }

    // Métodos auxiliares para obter parâmetros do JsonObject
    private static String getParam(JsonObject params, String paramName) throws Exception {
        if (params.has(paramName) && !params.get(paramName).isJsonNull()) {
            return params.get(paramName).getAsString();
        } else {
            throw new Exception("Parâmetro " + paramName + " não encontrado ou é nulo.");
        }
    }

    private static int getIntParam(JsonObject params) throws Exception {
        if (params.has("poltrona") && !params.get("poltrona").isJsonNull()) {
            return params.get("poltrona").getAsInt();
        }
        throw new Exception("Parâmetro " + "poltrona" + " não encontrado ou é nulo.");
    }

}