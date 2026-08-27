package verzelEvents.dto.response;

import verzelEvents.entity.ResultadoValidacao;
import java.time.LocalDateTime;

public record ValidacaoHistoryResponse(
    LocalDateTime dataHora,
    ResultadoValidacao resultado,
    String portariaNome
) {
}