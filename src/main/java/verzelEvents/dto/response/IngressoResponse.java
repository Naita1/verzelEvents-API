package verzelEvents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import verzelEvents.entity.Ingresso;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngressoResponse {
    private UUID id;
    private String status;
    private String qrHash;
    private String shareToken;
    private String eventoTitulo;
    private String assentoCodigo;
    private String codigoValidacao;
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