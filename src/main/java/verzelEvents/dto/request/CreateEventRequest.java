package verzelEvents.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateEventRequest {

    @NotBlank
    private String titulo;

    @NotBlank
    private String tipo;

    @NotNull
    private LocalDateTime dataHora;

    @NotBlank
    private String local;

    @NotNull
    @Positive
    private Integer capacidade;

    @NotNull
    @PositiveOrZero
    private BigDecimal preco;
}