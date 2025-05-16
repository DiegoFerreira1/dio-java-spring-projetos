# Passo 3 – Implementação básica de saldo e extrato

```java
package dio.desafio.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Repository;

import dio.desafio.model.Conta;
import dio.desafio.model.Transacao;

@Repository
public class ContaRepository {

    private List<Conta> contas = new ArrayList<>();

    public void salvar(Conta conta){
        contas.add(conta);
    }

    public Conta findById(Long id){
        return contas.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    private Transacao criarTransacao(String descricao, double valor){
        Transacao t = new Transacao();
        t.setData(LocalDateTime.now().minusDays((long) (Math.random() * 10)));
        t.setDescricao(descricao);
        t.setValor(valor);
        return t;
    }

}
```

### Implementando as classes e interface por partes, utilizando inicialmente o bando de dados em memória.

> A utilização da biblioteca **lombok** economiza muitas linhas de código dos `getters` e `setters`.

## Classe Conta 
- (model/Conta.java)

```java
package dio.desafio.model;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class Conta {

    private Long id;
    private String titular;
    private double saldo;
    private List<Transacao> transacoes;  
    
}
```

## Classe Transacao
- (model/Transacao.java)

```java
package dio.desafio.model;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Transacao {

    private LocalDateTime data;
    private String descricao;
    private double valor;
    
}
```

## Classe ContaService
- (service/ContaService.java)

```java
package dio.desafio.service;

import dio.desafio.model.Conta;

public interface ContaService {

    Conta findById(Long id); 
    
    void salvarConta(Conta conta);
       
    void transfer(Long origemId, Long destinoId, double valor);
}
```

## Classe ContaRepository
- (repository/ContaRepository.java)

- Anotação usada: `@Repository` - Definindo a classe como repositório

```java
package dio.desafio.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Repository;

import dio.desafio.model.Conta;
import dio.desafio.model.Transacao;

@Repository
public class ContaRepository {

    private List<Conta> contas = new ArrayList<>();

    public void salvar(Conta conta){
        contas.add(conta);
    }

    public Conta findById(Long id){
        return contas.stream()
            .filter(c -> c.getId().equals(id))
            .findFirst()
            .orElse(null);
    }

    private Transacao criarTransacao(String descricao, double valor){
        Transacao t = new Transacao();
        t.setData(LocalDateTime.now().minusDays((long) (Math.random() * 10)));
        t.setDescricao(descricao);
        t.setValor(valor);
        return t;
    }

}
```

## Classe ContaServiceImpl
- (service/impl/ContaServiceImpl.java)
- Anotação usada: `@Service` - Aqui implementamos a interface ContaService
- Anotação `@Autowired` - Instanciação simplificada
- Anotação Override `Override` - Reutilização do método da inferface

```java
package dio.desafio.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dio.desafio.model.Conta;
import dio.desafio.repository.ContaRepository;
import dio.desafio.service.ContaService;

@Service
public class ContaServiceImpl implements ContaService{

    @Autowired
    private ContaRepository contaRepository;

    @Override
    public Conta findById(Long id) {
        return contaRepository.findById(id);  
    }
   
}
```

## Classe ContaController
- (controller/ContaController.java)
- Anotação `@RestController` - Definindo um controller
- Anotação `@Requestmapping("/conta")` - defini endpoinp principal
- Anotação `@GetMapping("/{id}/saldo)` - endpoint id/saldo
- Anotação `@GetMapping("/{id}/extrado)` - endpoint id/extrato

```java
package dio.desafio.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dio.desafio.dto.TransferenciaDTO;
import dio.desafio.model.Conta;
import dio.desafio.service.ContaService;

@RestController
@RequestMapping("/conta")
public class ContaController {

    @Autowired
    private ContaService contaService;

    @PostMapping
    public void criarConta(@RequestBody Conta conta) {
        contaService.salvarConta(conta);
    }


    @GetMapping("/{id}/saldo")
    public double getSaldo(@PathVariable Long id){
        Conta conta = contaService.findById(id);
        return conta != null ? conta.getSaldo() : 0.0;
    }

    @GetMapping("/{id}/extrato")
    public Conta getExtrato(@PathVariable Long id){
        return contaService.findById(id);
    }

    @PostMapping("/transferencia")
    public void transferir(@RequestBody TransferenciaDTO dto) {
        contaService.transfer(dto.getContaOrigemId(), dto.getContaDestinoId(), dto.getValor());
    }
    
}
```