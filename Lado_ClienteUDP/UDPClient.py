import socket
import json
import logging

# Configuração do logger
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

class UDPClient:
    def __init__(self, hostname, port):
        self.server_address = (hostname, port)
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        logger.info(f"UDPClient initialized with server address: {self.server_address}")

    def send_request(self, request):
        try:
            # Serializa a mensagem inteira como JSON e então converte para bytes

            self.socket.sendto(request.encode('utf-8'), self.server_address)
            logger.info(f"Sent request: {request}")
        except Exception as e:
            logger.error(f"Failed to send request: {e}")

    def receive_response(self):
        try:
            # Recebe a resposta como um pacote de bytes
            packet, _ = self.socket.recvfrom(4096)  # Buffer de 4096 bytes
            json_response = packet.decode('utf-8')  # Decodifica de volta para string
            logger.info(f"Received response: {json_response}")
            return json_response  # Retorna como JSON
        except json.JSONDecodeError as e:
            logger.error(f"Failed to decode JSON: {e}")
            return None
        except Exception as e:
            logger.error(f"Failed to receive response: {e}")
            return None

    def close(self):
        self.socket.close()
        logger.info("Socket closed")

