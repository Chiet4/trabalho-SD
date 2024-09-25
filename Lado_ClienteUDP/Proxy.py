import json
from Mensagem import Message
from UDPClient import UDPClient
from Ticket import Ticket


class Proxy:
    def __init__(self, hostname, port):
        self.request_id = 0
        self.client = UDPClient(hostname, port)

    def criar_mensagem(self, methodId, objeto_complexo=None, dados_simples=None):

        self.request_id += 1
        message = Message()
        message.setMessageType(0)
        message.setMethodId(methodId)
        message.setRequestId(self.request_id)


        if objeto_complexo is not None:
            message.setArguments(objeto_complexo.to_dict())
        elif dados_simples is not None:
            message.setArguments(dados_simples)
        else:
            message.setArguments({})  # Caso nenhum dado seja fornecido, usa um dicionário vazio

        return message.to_json()

    def processar_resposta(self, resposta):

        try:
            message = json.loads(resposta)
            if message["messageType"] == 1:
                arguments = message["arguments"]

                if isinstance(arguments, str):
                    arguments_json = json.loads(arguments)
                else:
                    arguments_json = arguments

                return arguments_json
            return None
        except json.JSONDecodeError as e:
            print(f"Falha ao decodificar o JSON: {e}")
            return None

    def doOperation(self, requisicao):

        tentativas = 0
        while tentativas < self.client.max_retransmissao:
            self.client.enviar_solicitacao(requisicao)
            resposta = self.client.receber_resposta()

            if resposta:
                return resposta
            else:
                print(f"Tentativa {tentativas + 1} falhou. Retentando...")
                tentativas += 1

        print("Erro ao processar a resposta após várias tentativas.")
        return None

    def reservar_ticket(self, ticket: Ticket):
        requisicao = self.criar_mensagem("reservar_ticket", ticket)
        resposta = self.doOperation(requisicao)
        return self.processar_resposta(resposta)

    def atualizar_ticket(self, ticket_id, newTicket: Ticket):
        try:
            # Converte o objeto Ticket para um dicionário
            if isinstance(newTicket, Ticket):
                ticket_data = newTicket.to_dict()
            else:
                raise TypeError("newTicket não é uma instância de Ticket")

            # Adiciona o ticketId ao dicionário
            ticket_data["ticketId"] = ticket_id

            # Cria a mensagem de atualização com os argumentos necessários
            requisicao = self.criar_mensagem("atualizar_reserva", dados_simples=ticket_data)

            # Envia a requisição e processa a resposta
            resposta = self.doOperation(requisicao)
            return self.processar_resposta(resposta)
        except Exception as e:
            print(f"Erro ao atualizar o ticket: {e}")
            return None

    def cancelar_ticket(self, ticket_id):
        requisicao = self.criar_mensagem("cancelar_reserva", dados_simples={"ticketId": ticket_id})
        resposta = self.doOperation(requisicao)
        return self.processar_resposta(resposta)

    def consultar_reserva(self, cpf):
        requisicao = self.criar_mensagem("consultar_reserva", dados_simples={"cpf": cpf})
        resposta = self.doOperation(requisicao)
        return self.processar_resposta(resposta)

    def buscar_historico(self):
        requisicao = self.criar_mensagem("consultar_historico", dados_simples={})
        resposta = self.doOperation(requisicao)
        return self.processar_resposta(resposta)

    def close(self):
        self.client.close()

