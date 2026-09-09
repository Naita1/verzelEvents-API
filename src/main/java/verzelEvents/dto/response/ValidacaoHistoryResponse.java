package verzelEvents.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import verzelEvents.entity.ResultadoValidacao;
import java.time.LocalDateTime;

@Schema(description = "Objeto contendo o histórico de uma tentativa de validação na portaria")
public record ValidacaoHistoryResponse(
    @Schema(description = "Data e hora em que a validação ocorreu", example = "2024-12-31T20:00:00") LocalDateTime dataHora,
    @Schema(description = "Resultado da validação do ingresso", example = "VALIDO") ResultadoValidacao resultado,
    @Schema(description = "Nome do usuário da portaria que realizou a validação", example = "Portaria Demo") String portariaNome
) {
}