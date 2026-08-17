package verzelEvents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.UUID;

@Data
@AllArgsConstructor
public class IngressoResponse {
    private UUID id;
    private String status;
    private String qrHash;
    private String shareToken;
    private String eventoTitulo;
    private String assentoCodigo;
}