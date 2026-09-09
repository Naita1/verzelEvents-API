package verzelEvents.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import verzelEvents.entity.Ingresso;

import java.util.UUID;

@Schema(description = "Objeto contendo os dados do ingresso gerado após o pagamento")
public record IngressoResponse(
        @Schema(description = "ID único do ingresso", example = "123e4567-e89b-12d3-a456-426614174000") UUID id,
        @Schema(description = "Status do ingresso (ex: VALIDO, UTILIZADO)", example = "VALIDO") String status,
        @Schema(description = "Hash criptográfico do QR Code", example = "a1b2c3d4e5f6...") String qrHash,
        @Schema(description = "Token público para compartilhamento do ingresso", example = "abc123xyz") String shareToken,
        @Schema(description = "Título do evento vinculado", example = "Matrix Resurrections") String eventoTitulo,
        @Schema(description = "Código do assento reservado", example = "A1") String assentoCodigo,
        @Schema(description = "Código completo para validação na portaria (reservaId:hash)", example = "550e8400-e29b-41d4-a716-446655440000:a1b2c3d4e5f6...") String codigoValidacao
) {

    public static IngressoResponse fromEntity(Ingresso ingresso) {
        if (ingresso == null) {
            return null;
        }

        String assentoCod = (ingresso.getReserva() != null && ingresso.getReserva().getAssento() != null)
                ? ingresso.getReserva().getAssento().getCodigo()
                : null;

        String eventoTit = (ingresso.getReserva() != null && ingresso.getReserva().getEvento() != null)
                ? ingresso.getReserva().getEvento().getTitulo()
                : null;

        String codigoVal = (ingresso.getReserva() != null)
                ? ingresso.getReserva().getId() + ":" + ingresso.getQrHash()
                : ingresso.getQrHash();

        return new IngressoResponse(
                ingresso.getId(),
                ingresso.getStatus() != null ? ingresso.getStatus().name() : null,
                ingresso.getQrHash(),
                ingresso.getShareToken(),
                eventoTit,
                assentoCod,
                codigoVal
        );
    }
}