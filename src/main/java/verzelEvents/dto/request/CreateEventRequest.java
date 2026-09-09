package verzelEvents.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CreateEventRequest {

    @NotBlank(message = "O título é obrigatório")
    @Size(max = 150, message = "O título deve ter no máximo 150 caracteres")
    private String titulo;

    @NotBlank(message = "O tipo do evento é obrigatório")
    private String tipo;

    @NotNull(message = "A data e hora do evento são obrigatórias")
    @Future(message = "A data e hora do evento devem ser no futuro")
    private LocalDateTime dataHora;

    @NotBlank(message = "O local é obrigatório")
    @Size(max = 200, message = "O local deve ter no máximo 200 caracteres")
    private String local;

    @NotNull(message = "A capacidade é obrigatória")
    @Positive(message = "A capacidade deve ser maior que zero")
    @Max(value = 10000, message = "A capacidade máxima permitida é de 10.000 assentos")
    private Integer capacidade;

    @NotNull(message = "O preço é obrigatório")
    @PositiveOrZero(message = "O preço não pode ser negativo")
    @Digits(integer = 8, fraction = 2, message = "O preço deve ter no máximo 8 dígitos inteiros e 2 decimais")
    private BigDecimal preco;

    @Size(max = 255, message = "A URL da imagem não pode exceder 255 caracteres")
    private String imagemUrl;
}