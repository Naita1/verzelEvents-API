package verzelEvents.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EventoResponseTest {

    @Test
    @DisplayName("Deve instanciar o record EventoResponse e retornar os dados do evento corretamente")
    void deveInstanciarRecordComSucesso() {
        UUID id = UUID.randomUUID();
        LocalDateTime agora = LocalDateTime.now();
        BigDecimal preco = new BigDecimal("35.00");

        EventoResponse response = new EventoResponse(
                id,
                "Matrix Resurrections",
                "CINEMA",
                agora,
                "Sala 1 - Cine Verzel",
                10,
                preco,
                "Organizador Demo",
                "https://image.tmdb.org/t/p/w500/poster.jpg"
        );

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.titulo()).isEqualTo("Matrix Resurrections");
        assertThat(response.tipo()).isEqualTo("CINEMA");
        assertThat(response.dataHora()).isEqualTo(agora);
        assertThat(response.local()).isEqualTo("Sala 1 - Cine Verzel");
        assertThat(response.capacidade()).isEqualTo(10);
        assertThat(response.preco()).isEqualTo(preco);
        assertThat(response.organizadorNome()).isEqualTo("Organizador Demo");
        assertThat(response.imagemUrl()).contains("poster.jpg");
    }
}