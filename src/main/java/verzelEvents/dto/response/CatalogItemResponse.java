package verzelEvents.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
@Schema(description = "Objeto contendo as informações de um item (ex: filme) retornado pelo catálogo externo (TMDb)")
public record CatalogItemResponse(
        @Schema(description = "ID do item no catálogo externo", example = "603") String id,
        @Schema(description = "Título oficial do item", example = "Matrix") String titulo,
        @Schema(description = "Sinopse ou descrição do item", example = "Um hacker descobre a verdadeira natureza da realidade...") String descricao,
        @Schema(description = "URL completa para o pôster/imagem de capa", example = "https://image.tmdb.org/t/p/w500/f89U3ADj1EPmAwHQWiGfB32z7F5.jpg") String imagemUrl
) {}