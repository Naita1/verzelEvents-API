package verzelEvents.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ReservaTest {

    @Test
    @DisplayName("Deve garantir a igualdade entre reservas com base estritamente no ID")
    void deveTestarEqualsEHashCode() {
        UUID id = UUID.randomUUID();

        Reserva reserva1 = Reserva.builder()
                .id(id)
                .status(ReservaStatus.PENDENTE)
                .idempotencyKey("key-123")
                .build();

        Reserva reserva2 = Reserva.builder()
                .id(id)
                .status(ReservaStatus.PAGA)
                .idempotencyKey("key-456")
                .build();

        Reserva reservaDiferente = Reserva.builder()
                .id(UUID.randomUUID())
                .status(ReservaStatus.PENDENTE)
                .build();

        assertThat(reserva1).isEqualTo(reserva2);
        assertThat(reserva1).isNotEqualTo(reservaDiferente);
        assertThat(reserva1.hashCode()).isEqualTo(reserva2.hashCode());
    }

    @Test
    @DisplayName("Deve instanciar a entidade Reserva via Builder com todos os atributos")
    void deveInstanciarReservaComSucesso() {
        UUID id = UUID.randomUUID();
        LocalDateTime expirasEm = LocalDateTime.now().plusMinutes(5);
        Evento evento = Evento.builder().id(UUID.randomUUID()).build();
        Usuario cliente = Usuario.builder().id(UUID.randomUUID()).build();
        Assento assento = Assento.builder().id(UUID.randomUUID()).build();

        Reserva reserva = Reserva.builder()
                .id(id)
                .evento(evento)
                .cliente(cliente)
                .assento(assento)
                .status(ReservaStatus.PENDENTE)
                .expiresAt(expirasEm)
                .idempotencyKey("idem-key-abc")
                .build();

        assertThat(reserva.getId()).isEqualTo(id);
        assertThat(reserva.getEvento()).isEqualTo(evento);
        assertThat(reserva.getCliente()).isEqualTo(cliente);
        assertThat(reserva.getAssento()).isEqualTo(assento);
        assertThat(reserva.getStatus()).isEqualTo(ReservaStatus.PENDENTE);
        assertThat(reserva.getExpiresAt()).isEqualTo(expirasEm);
        assertThat(reserva.getIdempotencyKey()).isEqualTo("idem-key-abc");
    }
}