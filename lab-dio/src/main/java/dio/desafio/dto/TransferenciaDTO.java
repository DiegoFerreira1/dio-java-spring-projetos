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
