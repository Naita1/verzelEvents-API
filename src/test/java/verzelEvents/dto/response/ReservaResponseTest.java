package verzelEvents.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ReservaResponseTest {

    @Test
    @DisplayName("Deve instanciar o record ReservaResponse e retornar os atributos imutáveis corretamente")
    void deveInstanciarRecordComSucesso() {
        UUID id = UUID.randomUUID();
        LocalDateTime expirasEm = LocalDateTime.now().plusMinutes(5);

        ReservaResponse response = new ReservaResponse(
                id,
                "PENDENTE",
                expirasEm,
                "Matrix Resurrections",
                "A1",
                "Cliente Um"
        );

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.status()).isEqualTo("PENDENTE");
        assertThat(response.expiresAt()).isEqualTo(expirasEm);
        assertThat(response.eventoTitulo()).isEqualTo("Matrix Resurrections");
        assertThat(response.assentoCodigo()).isEqualTo("A1");
        assertThat(response.cliente()).isEqualTo("Cliente Um");
    }
}