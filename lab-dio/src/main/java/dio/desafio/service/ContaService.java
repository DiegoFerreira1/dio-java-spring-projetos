package dio.desafio.service;

import dio.desafio.model.Conta;

public interface ContaService {

    Conta buscarContaId(Long id); 
    
    void salvarConta(Conta conta);
       
    void transferir(Long origemId, Long destinoId, double valor);
}
