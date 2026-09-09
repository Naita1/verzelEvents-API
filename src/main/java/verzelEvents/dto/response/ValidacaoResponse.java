package verzelEvents.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Objeto contendo o resultado da tentativa de validação de um ingresso na portaria")
public record ValidacaoResponse(
        @Schema(description = "Status do resultado da validação (ex: VALIDO, INVALIDO, JA_UTILIZADO)", example = "VALIDO") String resultado,
        @Schema(description = "Mensagem amigável para exibição na tela do operador da portaria", example = "Ingresso validado com sucesso!") String mensagem
) {}