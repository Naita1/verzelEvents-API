package verzelEvents.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateReservaRequest {

    @NotNull(message = "O ID do evento é obrigatório")
    private UUID eventoId;

    @NotNull(message = "O ID do assento é obrigatório")
    private UUID assentoId;

    @Size(max = 100, message = "A chave de idempotência não pode exceder 100 caracteres")
    private String idempotencyKey;
}