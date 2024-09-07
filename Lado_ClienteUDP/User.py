from Proxy import Proxy

def main():
    hostname = 'localhost'  # Substitua pelo hostname do servidor
    port = 9876  # Substitua pela porta do servidor

    try:
        proxy = Proxy(hostname, port)

        # Exemplo de reserva de ticket
        cpf = "12345678900"
        data = "2024-09-01"
        hora = "10:00"
        origem = "Quixada"
        destino = "Fortaleza"
        nome = "Joao Silva"
        poltrona = 12

        print("Reservando ticket...")
        proxy.reservar_ticket(cpf, data, hora, origem, destino, nome, poltrona)

        # # Exemplo de atualização de reserva
        # ticket_id = "TICKET-12-8900-2024-09-01"
        # print("Atualizando reserva...")
        # response = proxy.atualizar_reserva(ticket_id, cpf, data, hora, origem, destino, "Joao", 15)
        # print(f"Resposta: {response}")
        #
        # #Exemplo de cancelamento de reserva
        # print("Cancelando reserva...")
        # response = proxy.cancelar_reserva("TICKET-12-8900-2024-09-01")
        # print(f"Resposta: {response}")

        # Exemplo de consulta de reserva
        # print("Consultando reserva...")
        # proxy.consultar_reserva(cpf)
        #print(f"Resposta: {response}")

    except Exception as e:
        print(f"Erro-cliente: {e}")
    finally:
        proxy.close()

if __name__ == "__main__":
    main()



# Metodo com interação com usuario
# def main():
#     hostname = 'localhost'  # Substitua pelo hostname do servidor
#     port = 9876  # Substitua pela porta do servidor
#
#     try:
#         proxy = Proxy(hostname, port)
#
#         while True:
#             print("\nEscolha uma opção:")
#             print("1. Reservar Ticket")
#             print("2. Atualizar Reserva")
#             print("3. Cancelar Reserva")
#             print("4. Consultar Reserva")
#             print("5. Sair")
#             opcao = input("Opção: ")
#
#             if opcao == '1':
#                 cpf = input("CPF: ")
#                 data = input("Data (YYYY-MM-DD): ")
#                 hora = input("Hora (HH:MM): ")
#                 origem = input("Origem: ")
#                 destino = input("Destino: ")
#                 nome = input("Nome: ")
#                 poltrona = int(input("Poltrona: "))
#                 proxy.reservar_ticket(cpf, data, hora, origem, destino, nome, poltrona)
#                 #print(f"ID do Ticket: {response}")
#
#             elif opcao == '2':
#                 ticket_id = input("ID do Ticket: ")
#                 cpf = input("CPF: ")
#                 data = input("Data (YYYY-MM-DD): ")
#                 hora = input("Hora (HH:MM): ")
#                 origem = input("Origem: ")
#                 destino = input("Destino: ")
#                 nome = input("Nome: ")
#                 poltrona = int(input("Poltrona: "))
#                 proxy.atualizar_reserva(ticket_id, cpf, data, hora, origem, destino, nome, poltrona)
#
#             elif opcao == '3':
#                 ticket_id = input("ID do Ticket: ")
#                 proxy.cancelar_reserva(ticket_id)
#
#             elif opcao == '4':
#                 cpf = input("CPF: ")
#                 proxy.consultar_reserva(cpf)
#
#             elif opcao == '5':
#                 break
#
#             else:
#                 print("Opção inválida. Tente novamente.")
#
#     except Exception as e:
#         print(f"Erro: {e}")
#     finally:
#         proxy.close()
#
# if __name__ == "__main__":
#     main()