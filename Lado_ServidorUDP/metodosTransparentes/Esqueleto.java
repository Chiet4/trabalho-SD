package com.anchietaalbano.trabalho.metodosTransparentes;

import com.anchietaalbano.trabalho.Passagem;
import com.anchietaalbano.trabalho.Resposta;
import com.anchietaalbano.trabalho.Ticket;
import com.anchietaalbano.trabalho.logs.LoggerColorido;
import com.anchietaalbano.trabalho.validates.ValidacaoDeDados;
import com.google.gson.JsonObject;
import java.util.List;
import java.util.function.Function;


public class Esqueleto {
    private final Passagem passagem;

    public Esqueleto() {
        passagem = new Passagem();
    }

    public Resposta reservar_ticket(JsonObject params) {
        return processarRequisicao(params, this::reservarTicketInterno, "reservar_ticket");
    }

    public Resposta atualizar_reserva(JsonObject params) {
        return processarRequisicao(params, this::atualizarReservaInterno, "atualizar_reserva");
    }

    public Resposta cancelar_reserva(JsonObject params) {
        return processarRequisicao(params, this::cancelarReservaInterno, "cancelar_reserva");
    }

    public Resposta consultar_reserva(JsonObject params) {
        return processarRequisicao(params, this::consultarReservaInterno, "consultar_reserva");
    }

    public Resposta consultar_historico() {
        return processarRequisicao(null, this::consultarHistoricoInterno, "consultar_historico");
    }


    // Métodos privados internos para processar as requisições
    private Resposta reservarTicketInterno(JsonObject params) {
    try {
        ValidacaoDeDados.validarTicket(params);
        Ticket ticket = criarTicket(params);
        String resultado = passagem.reservar_ticket(ticket.getCpf(), ticket.getData(), ticket.getHora(), ticket.getOrigem(), ticket.getDestino(), ticket.getNome(), ticket.getPoltrona());
        return Resposta.criado("Reserva criada com sucesso: " + resultado);
    } catch (Exception e) {
        return handleException(e, "reservar_ticket");
    }
}

private Resposta atualizarReservaInterno(JsonObject params) {
    try {
        String ticketId = getParam(params, "ticketId");

        if (!passagem.reservaExiste(ticketId)) {
            return Resposta.notFound("Erro: Reserva não encontrada ou já cancelada.");
        }

        ValidacaoDeDados.validarTicket(params);
        Ticket ticket = criarTicket(params);
        String resultado = passagem.atualizar_reserva(ticketId, ticket);
        return Resposta.ok("Reserva atualizada com sucesso: " + resultado);
    } catch (Exception e) {
        return handleException(e, "atualizar_reserva");
    }
}

private Resposta cancelarReservaInterno(JsonObject params) {
    try {
        String ticketId = getParam(params, "ticketId");

        if (!ValidacaoDeDados.validarCancelarReserva(params)) {
            return Resposta.badRequest("Erro: ticketId inválido.");
        }

        String resultado = passagem.cancelar_reserva(ticketId);
        return Resposta.ok("Reserva cancelada com sucesso: " + resultado);
    } catch (Exception e) {
        return handleException(e, "cancelar_reserva");
    }
}

private Resposta consultarReservaInterno(JsonObject params) {
    try {
        String cpf = getParam(params, "cpf");

        if (!ValidacaoDeDados.validarCPF(cpf)) {
            return Resposta.badRequest("Erro: CPF inválido.");
        }

        List<String> reservas = passagem.consultar_reserva(cpf);
        if (reservas.isEmpty()) {
            return Resposta.notFound("Nenhuma reserva encontrada para o CPF informado.");
        }

        return Resposta.ok(String.join(";", reservas));
    } catch (Exception e) {
        return handleException(e, "consultar_reserva");
    }
}

private Resposta consultarHistoricoInterno(JsonObject params) {
    try {
        List<String> historicos = passagem.consultar_historico();
        if(historicos.isEmpty()){
            return Resposta.notFound("Nenhuma movimentação de passagens ainda.");
        }
        return Resposta.ok(String.join(";", historicos));
    } catch (Exception e) {
        return handleException(e, "consultar_historico");
    }
}

// Método para processar exceções e logar erros
private Resposta handleException(Exception e, String metodo) {

    if (e.getMessage().contains("Validacao")){
        LoggerColorido.logErro("Erro: " + e.getMessage());
        return Resposta.badRequest(e.getMessage());
    }

    LoggerColorido.logErro("Erro interno ao processar " + metodo + ": " + e.getMessage());
    return Resposta.erroInterno("Erro interno ao processar " + metodo + ": " + e.getMessage());
}

// Método genérico para processar requisições com exceções
private Resposta processarRequisicao(JsonObject params, Function<JsonObject, Resposta> acao, String metodo) {
    try {
        LoggerColorido.logInfo("Processando requisição para " + metodo);
        return acao.apply(params);
    } catch (Exception e) {
        return handleException(e, metodo);
    }
}

// Criação do objeto Ticket a partir dos parâmetros Json
private Ticket criarTicket(JsonObject params) throws Exception {
    String cpf = getParam(params, "cpf");
    String nome = getParam(params, "nome");
    String data = getParam(params, "data");
    String hora = getParam(params, "hora");
    String origem = getParam(params, "origem");
    String destino = getParam(params, "destino");
    int poltrona = getIntParam(params, "poltrona");

    return new Ticket(cpf, nome, data, hora, origem, destino, poltrona);
}

// Método auxiliar para obter parâmetros do JsonObject
private String getParam(JsonObject params, String paramName) throws Exception {
    if (params.has(paramName) && !params.get(paramName).isJsonNull()) {
        return params.get(paramName).getAsString();
    } else {
        throw new Exception("Parâmetro " + paramName + " não encontrado ou é nulo.");
    }
}

// Método auxiliar para obter parâmetros inteiros do JsonObject
private int getIntParam(JsonObject params, String paramName) throws Exception {
    if (params.has(paramName) && !params.get(paramName).isJsonNull()) {
        return params.get(paramName).getAsInt();
    } else {
        throw new Exception("Parâmetro " + paramName + " não encontrado ou é nulo.");
    }
}
}
