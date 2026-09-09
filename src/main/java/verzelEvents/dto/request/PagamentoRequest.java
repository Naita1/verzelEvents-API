package verzelEvents.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class PagamentoRequest {

    @NotBlank(message = "O número do cartão é obrigatório")
    @Size(min = 13, max = 20, message = "O número do cartão deve ter entre 13 e 20 caracteres")
    private String numeroCartao;

    @NotBlank(message = "O nome no cartão é obrigatório")
    @Size(min = 2, max = 100, message = "O nome no cartão deve ter entre 2 e 100 caracteres")
    private String nomeCartao;
}