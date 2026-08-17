package verzelEvents.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
public class EventoResponse {
    private UUID id;
    private String titulo;
    private String tipo;
    private LocalDateTime dataHora;
    private String local;
    private Integer capacidade;
    private BigDecimal preco;
    private String organizadorNome;
}