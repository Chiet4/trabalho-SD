import json
from Mensagem import Message
from UDPClient import UDPClient


class Proxy:
    def __init__(self, hostname, port):
        self.request_id = 0
        self.client = UDPClient(hostname, port)

    def doOperation(self, args, params=None):
        self.request_id += 1

        if params is not None:
            # Cria a mensagem de requisição
            message = Message(args)
            message.setMessageType(0)  # 0 = Requisição
            message.setRequestId(self.request_id)
            message.setArguments(params)

            return message.to_json()  # Já gera uma string JSON
        else:
            # Processa a resposta
            if isinstance(args, str):
                message = json.loads(args)  # Desserializa a resposta se for string JSON
            else:
                message = args  # Se já for um dicionário, usa diretamente

            if message["messageType"] == 1:  # 1 = Resposta
                arguments = message["arguments"]

                if isinstance(arguments, str):
                    try:
                        arguments_json = json.loads(arguments)
                    except json.JSONDecodeError as e:
                        print(f"Falha ao decodificar o JSON: {e}")
                        return None
                else:
                    arguments_json = arguments

                response = arguments_json
                return response
            return None




    def reservar_ticket(self,cpf, nome, data, hora, origem, destino, poltrona):
        requisicao = self.doOperation("reservar_ticket", {
            "cpf": cpf,
            "nome": nome,
            "data": data,
            "hora": hora,
            "origem": origem,
            "destino": destino,

            "poltrona": poltrona
        })
        self.client.enviar_solicitacao(requisicao)
        resposta = self.client.receber_requisicao()
        return self.doOperation(resposta)

    def atualizar_ticket(self, ticket_id, cpf ,nome, data, hora, origem, destino,  poltrona):
        requisicao = self.doOperation("atualizar_reserva", {
            "ticketId": ticket_id,
            "cpf": cpf,
            "nome": nome,
            "data": data,
            "hora": hora,
            "origem": origem,
            "destino": destino,
            "poltrona": poltrona
        })
        self.client.enviar_solicitacao(requisicao)
        resposta = self.client.receber_requisicao()
        return self.doOperation(resposta)

    def cancelar_ticket(self, ticket_id):
        requisicao = self.doOperation("cancelar_reserva", {
            "ticketId": ticket_id
        })
        self.client.enviar_solicitacao(requisicao)
        resposta = self.client.receber_requisicao()
        return self.doOperation(resposta)

    def consultar_reserva(self, cpf):
        requisicao = self.doOperation("consultar_reserva", {
            "cpf": cpf
        })
        self.client.enviar_solicitacao(requisicao)
        resposta = self.client.receber_requisicao()
        return self.doOperation(resposta)

    def buscar_historico(self):
        requisicao = self.doOperation("consultar_historico", {})
        self.client.enviar_solicitacao(requisicao)
        resposta = self.client.receber_requisicao()
        return self.doOperation(resposta)

    def close(self):
        self.client.close()
