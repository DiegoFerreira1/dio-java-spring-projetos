# Passo 4 – Transformando a classe Model para Banco de dados H2

## Classe Conta

```java
package dio.desafio.model;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

@Entity(name = "tb_conta")
public class Conta {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titular;
    private double saldo;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Transacao> transacoes;  
    
}
```

## Classe Transacao

```java
package dio.desafio.model;

import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "tb_transacao")
public class Transacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private LocalDateTime data;
    private String descricao;
    private double valor;
    
}
```

## Interface ContaRepository

> Agora não precisa implementar os métodos: `findById, save, findAll`, etc., já são providos pelo JpaRepository.

```java
package dio.desafio.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dio.desafio.model.Conta;


@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {   
    

}
```

## Interface ContaService

```java
package dio.desafio.service;

import dio.desafio.model.Conta;

public interface ContaService {

    Conta buscarContaId(Long id); 
    
    void salvarConta(Conta conta);
       
    void transferir(Long origemId, Long destinoId, double valor);
}
```

## Classe ContaServiceImpl

> Aqui os metodo da biblioteca JPA são chamados 

```java
package dio.desafio.service.impl;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import dio.desafio.model.Conta;
import dio.desafio.model.Transacao;
import dio.desafio.repository.ContaRepository;
import dio.desafio.service.ContaService;
import jakarta.transaction.Transactional;


@Service
public class ContaServiceImpl implements ContaService{

    @Autowired
    private ContaRepository contaRepository;

    @Override
    public void salvarConta(Conta conta) {
        contaRepository.save(conta);
    }

    @Override
    public Conta buscarContaId(Long id) {
        return contaRepository.findById(id).orElse(null);  
    }

    @Transactional
    @Override
    public void transferir(Long origemId, Long destinoId, double valor) {
        Conta origem = contaRepository.findById(origemId)
            .orElseThrow(() -> new RuntimeException("Conta de origem não encontrada"));

        Conta destino = contaRepository.findById(destinoId)
            .orElseThrow(() -> new RuntimeException("Conta de destino não encontrada"));

        if (origem.getSaldo() < valor) {
            throw new IllegalArgumentException("Saldo insuficiente");
        }

        origem.setSaldo(origem.getSaldo() - valor);
        destino.setSaldo(destino.getSaldo() + valor);

        Transacao t1 = new Transacao();
        t1.setDescricao("Transferência enviada para conta " + destino.getId());
        t1.setValor(-valor);
        t1.setData(LocalDateTime.now());

        Transacao t2 = new Transacao();
        t2.setDescricao("Transferência recebida de conta " + origem.getId());
        t2.setValor(valor);
        t2.setData(LocalDateTime.now());

        origem.getTransacoes().add(t1);
        destino.getTransacoes().add(t2);

        //Importante para persistência
        contaRepository.save(origem);
        contaRepository.save(destino);
    }
   
}
```

## A classe TransferenciaDTO se mantem

```java
package dio.desafio.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransferenciaDTO {
    private Long contaOrigemId;
    private Long contaDestinoId;
    private double valor;
    
}
```

## Classe ContaController

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
        Conta conta = contaService.buscarContaId(id);
        return conta != null ? conta.getSaldo() : 0.0;
    }

    @GetMapping("/{id}/extrato")
    public Conta getExtrato(@PathVariable Long id){
        return contaService.buscarContaId(id);
    }

    @PostMapping("/transferencia")
    public void transferir(@RequestBody TransferenciaDTO dto) {
        contaService.transferir(dto.getContaOrigemId(), dto.getContaDestinoId(), dto.getValor());
    }
    
}
```