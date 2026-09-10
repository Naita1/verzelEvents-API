package verzelEvents.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class EventoTest {

    @Test
    @DisplayName("Deve garantir a igualdade entre eventos com base estritamente no ID")
    void deveTestarEqualsEHashCode() {
        UUID id = UUID.randomUUID();

        Evento evento1 = Evento.builder()
                .id(id)
                .titulo("Matrix Resurrections")
                .tipo("CINEMA")
                .build();

        Evento evento2 = Evento.builder()
                .id(id)
                .titulo("Outro Título")
                .tipo("TEATRO")
                .build();

        Evento eventoDiferente = Evento.builder()
                .id(UUID.randomUUID())
                .titulo("Matrix Resurrections")
                .build();

        assertThat(evento1).isEqualTo(evento2);
        assertThat(evento1).isNotEqualTo(eventoDiferente);
        assertThat(evento1.hashCode()).isEqualTo(evento2.hashCode());
    }

    @Test
    @DisplayName("Deve instanciar a entidade Evento via Builder com todos os atributos")
    void deveInstanciarEventoComSucesso() {
        UUID id = UUID.randomUUID();
        LocalDateTime agora = LocalDateTime.now();
        Usuario organizador = Usuario.builder().id(UUID.randomUUID()).nome("Organizador").build();

        Evento evento = Evento.builder()
                .id(id)
                .organizador(organizador)
                .titulo("Show do Ano")
                .tipo("SHOW")
                .dataHora(agora)
                .local("Arena Verzel")
                .capacidade(500)
                .preco(new BigDecimal("150.00"))
                .imagemUrl("https://image.com/poster.jpg")
                .build();

        assertThat(evento.getId()).isEqualTo(id);
        assertThat(evento.getTitulo()).isEqualTo("Show do Ano");
        assertThat(evento.getOrganizador().getNome()).isEqualTo("Organizador");
        assertThat(evento.getCapacidade()).isEqualTo(500);
    }
}