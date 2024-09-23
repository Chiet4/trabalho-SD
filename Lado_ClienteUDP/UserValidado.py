from datetime import datetime
from Proxy import Proxy
import pytz


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

        # metodo para chamar execução de metodos do lado do proxy
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
            try:
                data_passagem = datetime.strptime(data_passagem, '%Y-%m-%d')
                data_atual = self._data().date()
                if data_passagem.date() >= data_atual:
                    return True
                else:
                    print("Data da passagem deve ser para hoje ou para datas futuras.")
                    return False
            except ValueError:
                print("ERRO: Use apenas formato YYYY-MM-DD.")
                return False


        @staticmethod
        def _validar_cpf(cpf):
            if cpf.isdigit() and len(cpf) == 11:
                return True
            else:
                print("ERRO ao digitar CPF. Só pode conter apenas números e ter 11 dígitos.")
                return

        @staticmethod
        def _validar_nome(nome):
            while True:
                if nome.strip().isalpha():
                    return True
                else:
                    print("Nome errado. Deve conter apenas letras e espaços.")
                    nome = input("Nome: ")

        @staticmethod
        def _validar_data(data):
            while True:
                try:
                    # ve o formato da data
                    data_obj = datetime.strptime(data, "%Y-%m-%d")

                    # ve se a data é atual ou futura
                    if data_obj < datetime.now():
                        print("A data deve ser a atual ou uma data futura.")
                        data = input("Data (YYYY-MM-DD): ")
                    else:
                        return True
                except ValueError:
                    print("Data inválida. Use o formato YYYY-MM-DD.")
                    data = input("Data (YYYY-MM-DD): ")

        @staticmethod
        def _validar_hora(hora):
            while True:
                try:
                    # formato da hora
                    hora_obj = datetime.strptime(hora, "%H:%M")

                    # separar hora e minutos para verificar os limites
                    hora, minuto = hora.split(':')
                    hora = int(hora)
                    minuto = int(minuto)

                    if not (0 <= hora < 24 and 0 <= minuto < 60):
                        print("Hora inválida. Use o formato HH:MM e veja se a hora e minutos estão dentro dos limites.")
                        hora = input("Hora (HH:MM): ")
                    else:
                        return True
                except ValueError:
                    print("Hora inválida. Use o formato HH:MM.")
                    hora = input("Hora (HH:MM): ")

        @staticmethod
        def _validar_origem(origem):
            while True:
                if origem.strip():
                    return True
                else:
                    print("Origem nao pode esta vazia.")
                    origem = input("Origem: ")

        @staticmethod
        def _validar_destino(destino):
            while True:
                if destino.strip():
                    return True
                else:
                    print("Destino nao pode esta vazio.")
                    destino = input("Destino: ")

        @staticmethod
        def _validar_poltrona(poltrona, min_poltrona=1, max_poltrona=50):
            while True:
                if not str(poltrona).isdigit() or int(poltrona) <= 0:
                    print("numero de poltrona está errado. Coloque apenas número positivo.")
                    poltrona = input("Poltrona: ")
                    continue

                poltrona = int(poltrona)
                if min_poltrona <= poltrona <= max_poltrona:
                    return True

                print(f'O número da poltrona deve estar entre {min_poltrona} e {max_poltrona}.')
                poltrona = input("Poltrona: ")

        def reservar_ticket(self):
            cpf = input("CPF: ")
            if not self._validar_cpf(cpf):
                return

            nome = input("Nome: ")
            if not self._validar_nome(nome):
                return

            data = input("Data (YYYY-MM-DD): ")
            if not self._validar_data(data):
                return

            hora = input("Hora (HH:MM): ")
            if not self._validar_hora(hora):
                return

            origem = input("Origem: ")
            if not self._validar_origem(origem):
                return

            destino = input("Destino: ")
            if not self._validar_destino(destino):
                return

            poltrona = input("Poltrona: ")
            if not self._validar_poltrona(poltrona):
                return


            self._tratar_chamada_metodo_proxy(self.proxy.reservar_ticket, cpf, nome, data, hora, origem,
                                              destino, poltrona)

        def atualizar_reserva(self):
            ticket_id = input("ID do Ticket: ")

            cpf = input("CPF: ")
            if not self._validar_cpf(cpf):
                return

            nome = input("Nome: ")
            if not self._validar_nome(nome):
                return

            data = input("Data (YYYY-MM-DD): ")
            if not self._validar_data(data):
                return

            hora = input("Hora (HH:MM): ")
            if not self._validar_hora(hora):
                return

            origem = input("Origem: ")
            if not self._validar_origem(origem):
                return

            destino = input("Destino: ")
            if not self._validar_destino(destino):
                return

            poltrona = input("Poltrona: ")
            if not self._validar_poltrona(poltrona):
                return

            self._tratar_chamada_metodo_proxy(self.proxy.atualizar_ticket, ticket_id, cpf, nome, data, hora, origem, destino, poltrona)

        def cancelar_reserva(self):
            ticket_id = input("ID do Ticket: ")
            self._tratar_chamada_metodo_proxy(self.proxy.cancelar_ticket, ticket_id)

        def consultar_reserva(self):
            cpf = input('CPF: ')
            if not self._validar_cpf(cpf):
                return

            resposta = self._tratar_chamada_metodo_proxy(self.proxy.consultar_reserva, cpf)



        def consultar_historico(self):
            self._tratar_chamada_metodo_proxy(self.proxy.buscar_historico)

    cliente = Cliente('localhost', 9876)


if __name__ == '__main__':
    main()