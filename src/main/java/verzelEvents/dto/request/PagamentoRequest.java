package verzelEvents.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PagamentoRequest {

    @NotBlank
    private String numeroCartao;

    @NotBlank
    private String nomeCartao;
}