package verzelEvents.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CatalogItemResponseTest {

    @Test
    @DisplayName("Deve instanciar o record CatalogItemResponse via construtor e via Builder")
    void deveInstanciarRecordComSucesso() {
        CatalogItemResponse responseViaConstructor = new CatalogItemResponse(
                "603",
                "Matrix",
                "Um hacker descobre a verdadeira natureza da realidade...",
                "https://image.tmdb.org/t/p/w500/f89U3ADj1EPmAwHQWiGfB32z7F5.jpg"
        );

        CatalogItemResponse responseViaBuilder = CatalogItemResponse.builder()
                .id("603")
                .titulo("Matrix")
                .descricao("Um hacker descobre a verdadeira natureza da realidade...")
                .imagemUrl("https://image.tmdb.org/t/p/w500/f89U3ADj1EPmAwHQWiGfB32z7F5.jpg")
                .build();

        assertThat(responseViaConstructor).isEqualTo(responseViaBuilder);
        assertThat(responseViaConstructor.id()).isEqualTo("603");
        assertThat(responseViaConstructor.titulo()).isEqualTo("Matrix");
        assertThat(responseViaConstructor.descricao()).contains("hacker");
        assertThat(responseViaConstructor.imagemUrl()).contains("tmdb.org");
    }
}