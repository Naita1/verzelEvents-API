package verzelEvents.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Objeto contendo os dados de uma reserva temporária de assento")
public record ReservaResponse(
        @Schema(description = "ID único da reserva", example = "550e8400-e29b-41d4-a716-446655440000") UUID id,
        @Schema(description = "Status atual da reserva (ex: PENDENTE, PAGA, EXPIRADA)", example = "PENDENTE") String status,
        @Schema(description = "Data e hora limite para o pagamento antes da expiração", example = "2024-12-31T20:05:00") LocalDateTime expiresAt,
        @Schema(description = "Título do evento reservado", example = "Matrix Resurrections") String eventoTitulo,
        @Schema(description = "Código do assento reservado", example = "A1") String assentoCodigo,
        @Schema(description = "Nome do cliente que fez a reserva", example = "Cliente Um") String cliente
) {}