package verzelEvents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ReservaResponse {
    private UUID id;
    private String status;
    private LocalDateTime expiresAt;
    private String eventoTitulo;
    private String assentoCodigo;
    private String cliente;
}