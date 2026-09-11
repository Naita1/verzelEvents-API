package verzelEvents.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import verzelEvents.entity.Assento;
import verzelEvents.entity.Evento;
import verzelEvents.entity.Ingresso;
import verzelEvents.entity.IngressoStatus;
import verzelEvents.entity.Reserva;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class IngressoResponseTest {

    @Test
    @DisplayName("Deve instanciar o record IngressoResponse via construtor")
    void deveInstanciarRecordComSucesso() {
        UUID id = UUID.randomUUID();
        IngressoResponse response = new IngressoResponse(
                id, "VALIDO", "qrHash123", "shareToken123",
                "Matrix", "A1", "reservaId:qrHash123"
        );

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.codigoValidacao()).isEqualTo("reservaId:qrHash123");
    }

    @Test
    @DisplayName("Deve converter Ingresso para IngressoResponse via factory method fromEntity")
    void deveConverterFromEntityComSucesso() {
        UUID ingressoId = UUID.randomUUID();
        UUID reservaId = UUID.randomUUID();

        IngressoStatus statusEnum = IngressoStatus.values()[0];

        Ingresso ingresso = mock(Ingresso.class);
        Reserva reserva = mock(Reserva.class);
        Assento assento = mock(Assento.class);
        Evento evento = mock(Evento.class);

        when(ingresso.getId()).thenReturn(ingressoId);
        when(ingresso.getStatus()).thenReturn(statusEnum);
        when(ingresso.getQrHash()).thenReturn("hash_qr");
        when(ingresso.getShareToken()).thenReturn("share_token");
        when(ingresso.getReserva()).thenReturn(reserva);
        when(reserva.getId()).thenReturn(reservaId);
        when(reserva.getAssento()).thenReturn(assento);
        when(assento.getCodigo()).thenReturn("B4");
        when(reserva.getEvento()).thenReturn(evento);
        when(evento.getTitulo()).thenReturn("Inception");

        IngressoResponse response = IngressoResponse.fromEntity(ingresso);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(ingressoId);
        assertThat(response.status()).isEqualTo(statusEnum.name());
        assertThat(response.assentoCodigo()).isEqualTo("B4");
        assertThat(response.eventoTitulo()).isEqualTo("Inception");
        assertThat(response.codigoValidacao()).isEqualTo(reservaId + ":hash_qr");
    }

    @Test
    @DisplayName("Deve retornar null ao mapear uma entidade nula")
    void deveRetornarNullComEntidadeNula() {
        assertThat(IngressoResponse.fromEntity(null)).isNull();
    }
}