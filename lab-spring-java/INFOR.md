# 🧰 Padrões de Projeto em APIs com Spring Boot

Este projeto segue as boas práticas de arquitetura com Spring Boot, utilizando os principais **padrões de projeto** para tornar o código mais organizado, testável e escalável.

---

## 📐 Padrões Utilizados

### 🔧 1. Model-View-Controller (MVC)

**Separação de responsabilidades:**
- **Model** – Representa os dados e as regras de negócio (`@Entity`).
- **View** – Interface com o usuário (no caso de APIs, o JSON).
- **Controller** – Recebe requisições e retorna respostas (`@RestController`).

**🟩 Quando usar:**  
Sempre em aplicações Spring REST.

---

### 🧱 2. Repository Pattern

**Objetivo:**  
Abstrair e centralizar o acesso ao banco de dados.

**No Spring:**
```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {}
```

### 🧠 3. Service Layer Pattern

**Objetivo:**  
Isolar a lógica de negócio da camada de requisições.

**Exemplo:**
```java
@Service
public class UsuarioService {
    public Usuario salvar(Usuario u) {
        // regras de negócio
        return repository.save(u);
    }
}
```

**🟩 Quando usar:**  
Para centralizar regras de negócio e facilitar testes.

---

### 📦 4. DTO (Data Transfer Object)

**Objetivo:**  
Transferir dados entre camadas sem expor diretamente a entidade do banco

**Exemplo:**
```java

public class UsuarioDTO {
    private String nome;
    private String email;
}

```

**🟩 Quando usar:**  
Sempre que quiser manter uma API mais segura e coesa.

---

### 🔄 5. Mapper (ou Converter)

**Objetivo:**  
Converter entre DTOs e Entidades.

**Exemplo:**
```java

public class UsuarioMapper {
    public static UsuarioDTO toDTO(Usuario usuario) {
        UsuarioDTO dto = new UsuarioDTO();
        dto.setNome(usuario.getNome());
        dto.setEmail(usuario.getEmail());
        return dto;
    }

    public static Usuario toEntity(UsuarioDTO dto) {
        Usuario usuario = new Usuario();
        usuario.setNome(dto.getNome());
        usuario.setEmail(dto.getEmail());
        return usuario;
    }
}


```

**🟩 Quando usar:**  
Quando usar DTOs – ajuda a manter código limpo e evitar repetição.

---

### 🏗 6. Builder (opcional)

**Objetivo:**  
Criar objetos complexos passo a passo.
**Exemplo:**
```java
Usuario usuario = Usuario.builder()
                         .nome("João")
                         .email("joao@email.com")
                         .build();

```

**🟩 Quando usar:**  
Quando há muitos atributos opcionais.
---

### 🧪 7. Factory (opcional)

**Objetivo:**  
Encapsular a lógica de criação de objetos.
**Exemplo:**
```java
public class UsuarioFactory {
    public static Usuario criarUsuarioPadrao() {
        return Usuario.builder()
                      .nome("Usuário Padrão")
                      .email("padrao@email.com")
                      .build();
    }
}

```

**🟩 Quando usar:**  
Quando há necessidade de criar objetos com lógica específica, como em testes ou cenários diferentes.
---

## 🧱 Padrões de Projeto estudados no curso DIO

### 🔒 Singleton

**Objetivo:**  
Garantir que uma classe tenha apenas **uma instância** e fornecer um ponto global de acesso a ela.

**Exemplo:**
```java
public class Logger {
    private static Logger instance;

    private Logger() {}

    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void log(String msg) {
        System.out.println("LOG: " + msg);
    }
}

```

**🟩 Quando usar:**  
- Quando precisa de uma única instância compartilhada (ex: logs, conexões, cache).

- Cuidado com concorrência em aplicações multi-thread (use synchronized ou enum em casos complexos).
---


### 🔄 Strategy

**Objetivo:**  
Permitir que uma **família de algoritmos** seja definida e tornar cada um deles intercambiável.
O algoritmo pode variar independentemente dos clientes que o utilizam.

**Exemplo:**
```java
public interface PagamentoStrategy {
    void pagar(double valor);
}

public class CartaoCredito implements PagamentoStrategy {
    public void pagar(double valor) {
        System.out.println("Pagando com cartão de crédito: " + valor);
    }
}

public class Boleto implements PagamentoStrategy {
    public void pagar(double valor) {
        System.out.println("Pagando com boleto: " + valor);
    }
}

// Uso:
PagamentoStrategy estrategia = new CartaoCredito();
estrategia.pagar(100.0);


```

**🟩 Quando usar:**  
- Quando há várias formas de executar um comportamento e você quer trocar em tempo de execução.

- Ex: cálculos de frete, formas de pagamento, autenticação.
---

### 🎯 Facade

**Objetivo:**  
Fornecer uma interface simplificada para um conjunto de classes mais complexas.
**Exemplo:**
```java
public class EmailService {
    public void enviarEmail(String destino, String assunto, String corpo) {
        // lógica de envio
        System.out.println("Email enviado para " + destino);
    }
}

public class ClienteFacade {
    private EmailService emailService = new EmailService();

    public void registrarCliente(String nome, String email) {
        // salvar cliente, etc.
        emailService.enviarEmail(email, "Bem-vindo!", "Olá " + nome + ", obrigado por se registrar!");
    }
}


```

**🟩 Quando usar:**  
- Para esconder complexidade de subsistemas internos.

- Ex: serviços de envio de e-mail, APIs externas, bibliotecas complexas.
---

## 🧩 Tabela Resumo de Padrões de Projeto

| **Padrão**         | **Quando usar**                                                |
|--------------------|----------------------------------------------------------------|
| **MVC**            | Sempre em APIs com Spring Boot                                 |
| **Repository**     | Sempre que acessar o banco de dados                            |
| **Service**        | Para isolar lógica de negócio                                   |
| **DTO + Mapper**   | Para proteger e modelar os dados de entrada/saída              |
| **Builder**        | Para construir objetos com atributos opcionais                 |
| **Factory**        | Para encapsular regras na criação de objetos                   |
| **Singleton**      | Quando precisa de uma única instância (ex: log, config)        |
| **Strategy**       | Quando há comportamentos alternáveis em tempo de execução      |
| **Facade**         | Quando quiser simplificar a interação com sistemas complexos   |

com
└── exemplo
    └── meuapp
        ├── controller    # Pacote para controladores REST
        ├── service       # Pacote para serviços de lógica de negócios
        ├── repository    # Pacote para repositórios JPA
        ├── model         # Pacote para entidades (modelos)
        ├── dto           # Pacote para Data Transfer Objects (DTOs)
        ├── exception     # Pacote para exceções personalizadas
        ├── config        # Pacote para configurações
        └── MeuappApplication.java  # Classe principal com @SpringBootApplication

## Explicação dos Pacotes:

### controller:

Contém as classes responsáveis pela camada de controle (REST API). As classes do controlador lidam com requisições HTTP e delegam o processamento para os serviços.

Exemplo: UsuarioController.java

### service:

Contém a lógica de negócios. A camada de serviço deve ser responsável por orquestrar as operações do sistema e interagir com os repositórios para persistir ou recuperar dados.

Exemplo: UsuarioService.java

### repository:

Contém as interfaces que extendem JpaRepository ou CrudRepository. Elas são responsáveis pelas operações de persistência de dados.

Exemplo: UsuarioRepository.java

### model (ou entity):

Contém as classes de entidades JPA que são mapeadas para as tabelas no banco de dados.

Exemplo: Usuario.java

### dto (Data Transfer Objects):

Contém classes simples que são usadas para transferir dados entre camadas, especialmente entre o controlador e o cliente. Elas podem ser úteis para evitar expor diretamente as entidades JPA nas APIs.

Exemplo: UsuarioDTO.java

### exception:

Contém classes para exceções personalizadas e manipuladores globais de exceção (como um @ControllerAdvice para tratamento de erros).

Exemplo: ResourceNotFoundException.java ou GlobalExceptionHandler.java

### config:

Contém classes de configuração para o Spring Boot, como configurações de segurança, configurações de banco de dados ou configurações do próprio Spring.

Exemplo: WebSecurityConfig.java

### MeuappApplication.java:

A classe principal que contém o método main e é anotada com @SpringBootApplication. É o ponto de entrada da aplicação.
