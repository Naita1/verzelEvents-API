package verzelEvents.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.UUID;

@Data
public class CreateReservaRequest {

    @NotNull
    private UUID eventoId;

    @NotNull
    private UUID assentoId;

    private String idempotencyKey;
}