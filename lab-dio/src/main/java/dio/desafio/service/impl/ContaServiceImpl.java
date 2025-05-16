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
