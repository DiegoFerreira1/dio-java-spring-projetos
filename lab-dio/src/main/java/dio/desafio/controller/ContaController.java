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
