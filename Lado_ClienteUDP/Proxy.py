from UDPClient import UDPClient
import json
import logging

# Configuração do logger
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class Proxy:
    def __init__(self, hostname, port):
        self.request_id = 0
        self.client = UDPClient(hostname, port)

    def reservar_ticket(self, cpf, data, hora, origem, destino, nome, poltrona):
        request = self.doOperation("reservar_ticket", {
            "cpf": cpf,
            "data": data,
            "hora": hora,
            "origem": origem,
            "destino": destino,
            "nome": nome,
            "poltrona": poltrona
        })
        self.client.send_request(request)
        response = self.client.receive_response()
        if response:
            response = self.doOperation(response)
            logger.info(f"Response for reservar_ticket: {response}")
            print(f"Resposta: {response}")
            return response
        else:
            logger.error("No response received from server")
            return None

    def atualizar_reserva(self, ticket_id, cpf, data, hora, origem, destino, nome, poltrona):
        request = self.doOperation("atualizar_reserva", {
            "ticketId": ticket_id,
            "cpf": cpf,
            "data": data,
            "hora": hora,
            "origem": origem,
            "destino": destino,
            "nome": nome,
            "poltrona": poltrona
        })
        self.client.send_request(request)
        response = self.client.receive_response()
        if response:
            response = self.doOperation(response)
            print(f"Resposta: {response}")
            return response
        else:
            logger.error("No response received from server")
            return None

    def cancelar_reserva(self, ticket_id):
        request = self.doOperation("cancelar_reserva", {
            "ticketId": ticket_id
        })
        self.client.send_request(request)
        response = self.client.receive_response()
        if response:
            response = self.doOperation(response)
            print(f"Resposta: {response}")
            return response
        else:
            logger.error("No response received from server")
            return None

    def consultar_reserva(self, cpf):
        request = self.doOperation("consultar_reserva", {
            "cpf": cpf
        })
        self.client.send_request(request)
        response = self.client.receive_response()
        if response:
            response = self.doOperation(response)
            print(f"Resposta: {response}")
            return response
        else:
            logger.error("No response received from server")
            return None

    def doOperation(self, messageType, params=None):
        # Incrementando o request_id para cada operação
        self.request_id += 1

        if params is not None:
            # Criação da mensagem de requisição
            message = {
                "messageType": 0,  # 0 = Request
                "requestId": self.request_id,
                "methodId": messageType,
                "arguments": params
            }
            # Serialização da mensagem em JSON
            return json.dumps(message)
        else:
            # Processamento da resposta
            message = json.loads(messageType)  # Desserializa a resposta JSON

            if message["messageType"] == 1:  # 1 = Reply
                arguments = message["arguments"]

                if isinstance(arguments, str):
                    try:
                        arguments_json = json.loads(arguments)
                    except json.JSONDecodeError as e:
                        logger.error(f"Failed to decode JSON: {e}")
                        return None
                else:
                    arguments_json = arguments
                # Verificar se houve sucesso na operação
                response = {
                    "result": arguments_json,
                    "status": message["status"]
                }
                return response

    def close(self):
        self.client.close()

