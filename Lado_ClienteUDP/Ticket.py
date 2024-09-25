class Ticket:
    def __init__(self, cpf, nome, data, hora, origem, destino, poltrona):
        self.cpf = cpf
        self.nome = nome
        self.data = data
        self.hora = hora
        self.origem = origem
        self.destino = destino
        self.poltrona = poltrona

    def to_dict(self):
        """
        Converte o objeto Ticket para um dicionário para facilitar a serialização.
        """
        return {
            "cpf": self.cpf,
            "nome": self.nome,
            "data": self.data,
            "hora": self.hora,
            "origem": self.origem,
            "destino": self.destino,
            "poltrona": self.poltrona
        }

    @staticmethod
    def from_dict(dados):
        """
        Converte um dicionário em um objeto Ticket.
        """
        return Ticket(
            dados["cpf"],
            dados["nome"],
            dados["data"],
            dados["hora"],
            dados["origem"],
            dados["destino"],
            dados["poltrona"]
        )
