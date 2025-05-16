# Projeto de laboratório Spring Boot

## Sugestões do CHAT GPT:

- Tela de saldo e extrato – mostra o saldo da conta e as últimas transações.

- Transferência entre contas – permite transferir dinheiro para outra conta.

- Pagamento de boletos – digitar ou escanear código de barras para pagar boletos.

- Gestão de cartões – visualizar e bloquear/desbloquear cartões.

- Login com autenticação de dois fatores (2FA) – segurança de acesso.

- Investimentos – mostrar saldo investido, rentabilidade, etc.

- Simulação de empréstimo ou financiamento.

## Escolha da funcionalidade para o projeto

- Tela sugerida: "**Tela de saldo e extrato**", pois ela:

- Pode ser feita com uma API REST.

- Permite listar transações.

- Mostra como seria estruturado o backend de um app real.

> É uma boa base para depois expandir com mais funcionalidades como transferência ou pagamento.

## Projeto: API de Saldo e Extrato Bancário (Spring Boot)

### Estrutura básica:

- **ContaController** – expõe endpoints */saldo* e */extrato*

- **ContaService** – lógica de negócio

- **ContaRepository** – simula um banco de dados

- **Conta e Transacao** – classes de modelo

> Aqui temos uma ideia dos `packages` necessários pra projeto.

### Exemplo de endpoints:

- **GET /conta/{id}/saldo** – retorna o saldo atual da conta

- **GET /conta/{id}/extrato** – retorna a lista de transações da conta

-----------------------------------------------------------------------------

# Passo 1 - Configurações do projeto

No site [SpringInitializr](https://start.spring.io/)

- Escolher o `Maven`;
- Adicionar as dependências necessárias:
    - Spring Data JPA
    - Banco de dados em memória (H2) para teste
    - Banco de dados PostgreSQL
    - Lombok 
- Adicionar uma depêndencia externa do site [OpenAiSwagger](https://springdoc.org/)
    - Link do servidor: http://localhost:8080/swagger-ui.html

# Passo 2 - Teste inicial e criação dos pacotes e classes java

-  **controller** 
    - ContaController (Classe)

- **service** 
    - ContaService (Interface)
    - **impl** - package extra para implementar a interface ContaService
        - ContaServiceImpl (Classe)

- **repository** 
    - ContaRepository (Classe)

- **model** 
    - Conta e Transacao (Classes)


