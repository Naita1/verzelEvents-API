package verzelEvents.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import verzelEvents.entity.AssentoStatus;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AssentoResponseTest {

    @Test
    @DisplayName("Deve instanciar o record AssentoResponse e retornar seus atributos corretamente")
    void deveInstanciarRecordComSucesso() {
        UUID id = UUID.randomUUID();
        AssentoResponse response = new AssentoResponse(id, "B12", AssentoStatus.LIVRE);

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.codigo()).isEqualTo("B12");
        assertThat(response.status()).isEqualTo(AssentoStatus.LIVRE);
    }
}