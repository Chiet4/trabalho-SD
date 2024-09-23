import socket
import json

class UDPClient:
    """
    Envia os dados para o servidor e recebe as respostas. Implementa tanto erro de rede (timeout) quanto socket.
    __init__: inicializa o socket com porta e endereço.
    enviar_solicitacao: envia dados serializados para o servidor.
    receber_requisicao: recebe respostas do servidor.
    """

    def __init__(self, hostname, port, timeout=1, max_retransmissao=3):  # Timeout reduzido para testes
        self.endereco_servidor = (hostname, port)
        self.socket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
        self.timeout = timeout
        self.max_retransmissao = max_retransmissao
        self.socket.settimeout(self.timeout)
        self.requisicao = None

    def enviar_solicitacao(self, requisicao):
        try:
            # Envia a mensagem como bytes após serialização
            self.socket.sendto(requisicao.encode('utf-8'), self.endereco_servidor)
            self.requisicao = requisicao
            #print(f"Requisição enviada: {requisicao}")
        except Exception as e:
            print(f"Falha ao enviar a requisição: {e}")

    def receber_requisicao(self):
        tentativas = 0

        while tentativas < self.max_retransmissao:
            try:

                dados_recebidos, endereco = self.socket.recvfrom(512)
                resposta = json.loads(dados_recebidos.decode('utf-8'))

                return resposta
            except socket.timeout:
                tentativas += 1
                # Reenvia a solicitação em caso de timeout
                print(f"Tentativa {tentativas + 1}: Reenviando a solicitação...")
                self.enviar_solicitacao(self.requisicao)
            except socket.error as erro:
                print(f"UDPClient: Erro ao receber a resposta: {erro}")
                tentativas += 1  # Incrementa tentativas mesmo em caso de erro
                # Reenvia a solicitação em caso de erro
                print(f"Tentativa {tentativas + 1}: Reenviando a solicitação após erro...")
                self.enviar_solicitacao(self.requisicao)

        print("Número máximo de tentativas alcançado. Não foi possível estabelecer resposta com o servidor.")
        return None

    def close(self):
        self.socket.close()
        print("Conexão fechada!")

def main():
    hostname = 'localhost'  # Substitua pelo endereço do servidor
    port = 9876  # Substitua pela porta do servidor
    requisicao = json.dumps({"mensagem": "Olá, servidor!"})  # Exemplo de requisição

    cliente = UDPClient(hostname, port)
    cliente.enviar_solicitacao(requisicao)
    resposta = cliente.receber_requisicao()

    if resposta:
        print("Resposta do servidor:", resposta)
    else:
        print("Não foi possível obter uma resposta do servidor.")

    cliente.close()

if __name__ == "__main__":
    main()
