package com.anchietaalbano.trabalho;

import com.google.gson.JsonObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.logging.Logger;

public class ValidacaoDeDados {

    private static final Logger logger = Logger.getLogger(ValidacaoDeDados.class.getName());

    public static boolean validarTicket(JsonObject params) {
        boolean result;
        try {
            String cpf = params.get("cpf").getAsString();
            String nome = params.get("nome").getAsString();
            String data = params.get("data").getAsString();
            String hora = params.get("hora").getAsString();
            String origem = params.get("origem").getAsString();
            String destino = params.get("destino").getAsString();
            int poltrona = params.get("poltrona").getAsInt();

            // Validações para cada argumento
            result = validarCPF(cpf) &&
                    validarNome(nome) &&
                    validarData(data) &&
                    validarHora(hora) &&
                    validarOrigemDestino(origem) &&
                    validarOrigemDestino(destino) &&
                    validarPoltrona(poltrona);

        } catch (Exception e) {
            logger.severe("Erro na validação dos dados: " + e.getMessage());
            result = false;
        }
        return result;
    }

    private static boolean validarCPF(String cpf) {
        if (cpf != null && cpf.matches("\\d{11}")) {
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

    public static boolean validarConsultarReserva(JsonObject params) {
        return params.has("cpf") && validarCPF(params.get("cpf").getAsString());
    }

}
