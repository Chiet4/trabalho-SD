import socket
import json

class UDPClient:


    def __init__(self, hostname, port, timeout=1, max_retransmissao=3):  # Timeout reduzido para testes
        self.endereco_servidor = (hostname, port)
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.timeout = timeout
        self.max_retransmissao = max_retransmissao
        self.socket.settimeout(self.timeout)
        self.requisicao = None

    def enviar_solicitacao(self, requisicao):
        try:
            self.socket.sendto(requisicao.encode('utf-8'), self.endereco_servidor)
            self.requisicao = requisicao
        except Exception as e:
            print(f"Falha ao enviar a requisição: {e}")

    def receber_resposta(self):
        try:
            dados_recebidos, endereco = self.socket.recvfrom(512)
            resposta = dados_recebidos.decode('utf-8')
            return resposta
        except socket.timeout:
            print("Erro de timeout.")
            return None
        except Exception as e:
            print(f"Erro ao receber a resposta: {e}")
            return None

    def close(self):
        self.socket.close()
        print("Conexão fechada!")



