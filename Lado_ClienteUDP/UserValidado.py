from datetime import datetime
from Proxy import Proxy
import pytz
from Ticket import Ticket


def main():
    class Cliente:
        def __init__(self, hostname, porta):
            self.proxy = Proxy(hostname, porta)
            self.painel()

        def painel(self):
            while True:
                print("\nEscolha uma opção:")
                print("1. Reservar Ticket")
                print("2. Atualizar Reserva")
                print("3. Cancelar Reserva")
                print("4. Consultar Reserva")
                print("5. Consultar Histórico")
                print("6. Sair")

                try:
                    opcao = int(input("Opção: "))
                except ValueError:
                    print("ERRO: Digite só numeros")
                    continue

                if opcao == 1:
                    self.reservar_ticket()
                elif opcao == 2:
                    self.atualizar_reserva()
                elif opcao == 3:
                    self.cancelar_reserva()
                elif opcao == 4:
                    self.consultar_reserva()
                elif opcao == 5:
                    self.consultar_historico()
                elif opcao == 6:
                    break
                else:
                    print("Opção errada.Tente novamente.")

            self.proxy.close()


        def _tratar_chamada_metodo_proxy(self, metodo_proxy, *args):
            try:
                resposta = metodo_proxy(*args)
                if resposta is not None:
                    print(f'Resposta recebida: {resposta}')
                else:
                    print('Nenhuma resposta recebida ou erro.')
                return resposta
            except Exception as erro:
                print(f'Erro ao executar: {erro}')
                return None

        @staticmethod
        def _data():
            fuso_BR = pytz.timezone('Brazil/East')
            horario_BR = datetime.now(fuso_BR)
            return horario_BR

        def _validar_validade_passagem(self, data_passagem):
            while True:
                try:
                    data_passagem = datetime.strptime(data_passagem, '%Y-%m-%d')
                    data_atual = self._data().date()
                    if data_passagem.date() >= data_atual:
                        return data_passagem
                    else:
                        print("Data da passagem deve ser para hoje ou para datas futuras.")
                        data_passagem = input("Digite a data novamente (YYYY-MM-DD): ")
                except ValueError:
                    print("ERRO: Use o formato YYYY-MM-DD.")

        @staticmethod
        def _validar_cpf(cpf):
            while True:
                if cpf.isdigit() and len(cpf) == 11:
                    return cpf
                else:
                    print("ERRO: O CPF deve conter 11 dígitos numéricos.")
                    cpf = input("Digite o CPF novamente: ")

        @staticmethod
        def _validar_nome(nome):
            while True:
                if nome.strip().isalpha():
                    return nome
                else:
                    print("ERRO: O nome deve conter apenas letras.")
                    nome = input("Digite o nome novamente: ")

        @staticmethod
        def _validar_data(data):
            while True:
                try:
                    data_obj = datetime.strptime(data, "%Y-%m-%d")
                    if data_obj >= datetime.now():
                        return data
                    else:
                        print("A data deve ser atual ou futura.")
                        data = input("Digite a data novamente (YYYY-MM-DD): ")
                except ValueError:
                    print("ERRO: Use o formato YYYY-MM-DD.")
                    data = input("Digite a data novamente (YYYY-MM-DD): ")

        @staticmethod
        def _validar_hora(hora):
            while True:
                try:
                    hora_obj = datetime.strptime(hora, "%H:%M")
                    return hora
                except ValueError:
                    print("ERRO: Use o formato HH:MM.")
                    hora = input("Digite a hora novamente (HH:MM): ")

        @staticmethod
        def _validar_origem(origem):
            while True:
                if origem.strip():
                    return origem
                else:
                    print("ERRO: A origem não pode estar vazia.")
                    origem = input("Digite a origem novamente: ")

        @staticmethod
        def _validar_destino(destino):
            while True:
                if destino.strip():
                    return destino
                else:
                    print("ERRO: O destino não pode estar vazio.")
                    destino = input("Digite o destino novamente: ")

        @staticmethod
        def _validar_poltrona(poltrona, min_poltrona=1, max_poltrona=50):
            while True:
                if poltrona.isdigit() and min_poltrona <= int(poltrona) <= max_poltrona:
                    return str(poltrona)
                else:
                    print(f'ERRO: O número da poltrona deve estar entre {min_poltrona} e {max_poltrona}.')
                    poltrona = input(f"Digite o número da poltrona novamente ({min_poltrona}-{max_poltrona}): ")

        def reservar_ticket(self):
            cpf = self._validar_cpf(input("CPF: "))
            nome = self._validar_nome(input("Nome: "))
            data = self._validar_data(input("Data (YYYY-MM-DD): "))
            hora = self._validar_hora(input("Hora (HH:MM): "))
            origem = self._validar_origem(input("Origem: "))
            destino = self._validar_destino(input("Destino: "))
            poltrona = self._validar_poltrona(input("Poltrona: "))

            ticket = Ticket(cpf,nome,data,hora,origem,destino,poltrona)

            self._tratar_chamada_metodo_proxy(self.proxy.reservar_ticket, ticket)

        def atualizar_reserva(self):
            ticket_id = input("ID do Ticket: ")
            cpf = self._validar_cpf(input("CPF: "))
            nome = self._validar_nome(input("Nome: "))
            data = self._validar_data(input("Data (YYYY-MM-DD): "))
            hora = self._validar_hora(input("Hora (HH:MM): "))
            origem = self._validar_origem(input("Origem: "))
            destino = self._validar_destino(input("Destino: "))
            poltrona = self._validar_poltrona(input("Poltrona: "))

            ticket = Ticket(cpf, nome, data, hora, origem, destino, poltrona)

            self._tratar_chamada_metodo_proxy(self.proxy.atualizar_ticket, ticket_id, ticket)

        def cancelar_reserva(self):
            ticket_id = input("ID do Ticket: ")
            self._tratar_chamada_metodo_proxy(self.proxy.cancelar_ticket, ticket_id)

        def consultar_reserva(self):
            cpf = self._validar_cpf(input("CPF: "))
            self._tratar_chamada_metodo_proxy(self.proxy.consultar_reserva, cpf)

        def consultar_historico(self):
            self._tratar_chamada_metodo_proxy(self.proxy.buscar_historico)

    cliente = Cliente('localhost', 9876)


if __name__ == '__main__':
    main()
