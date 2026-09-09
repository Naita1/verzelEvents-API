package verzelEvents.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Objeto contendo os detalhes de um evento publicado")
public record EventoResponse(
        @Schema(description = "ID único do evento", example = "550e8400-e29b-41d4-a716-446655440000") UUID id,
        @Schema(description = "Título do evento", example = "Matrix Resurrections") String titulo,
        @Schema(description = "Tipo do evento (ex: CINEMA, SHOW, TEATRO)", example = "CINEMA") String tipo,
        @Schema(description = "Data e hora programada para o evento", example = "2024-12-31T20:00:00") LocalDateTime dataHora,
        @Schema(description = "Local onde ocorrerá o evento", example = "Sala 1 - Cine Verzel") String local,
        @Schema(description = "Capacidade total de assentos do evento", example = "10") Integer capacidade,
        @Schema(description = "Preço unitário do ingresso", example = "35.00") BigDecimal preco,
        @Schema(description = "Nome do organizador que criou o evento", example = "Organizador Demo") String organizadorNome,
        @Schema(description = "URL da imagem/pôster do evento", example = "https://image.tmdb.org/t/p/w500/f89U3ADj1EPmAwHQWiGfB32z7F5.jpg") String imagemUrl
) {}