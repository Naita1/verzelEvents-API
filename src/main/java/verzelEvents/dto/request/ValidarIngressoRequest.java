package verzelEvents.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class ValidarIngressoRequest {

    @NotBlank(message = "O código do ingresso é obrigatório")
    @Size(max = 200, message = "O código do ingresso não pode exceder 200 caracteres")
    private String codigo;

    @NotNull(message = "O ID do evento é obrigatório")
    private UUID eventoId;
}