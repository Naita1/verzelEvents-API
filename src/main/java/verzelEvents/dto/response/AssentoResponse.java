package verzelEvents.dto.response;

import verzelEvents.entity.AssentoStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

@Schema(description = "Objeto de transferência representando um assento e seu status atual")
public record AssentoResponse(
        @Schema(description = "ID único do assento", example = "123e4567-e89b-12d3-a456-426614174000") UUID id,
        @Schema(description = "Código do assento no mapa", example = "A1") String codigo,
        @Schema(description = "Status atual do assento (LIVRE, RESERVADO, VENDIDO)") AssentoStatus status
) {}