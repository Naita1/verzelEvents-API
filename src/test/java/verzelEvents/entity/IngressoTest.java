package verzelEvents.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class IngressoTest {

    @Test
    @DisplayName("Deve garantir a igualdade entre ingressos com base estritamente no ID")
    void deveTestarEqualsEHashCode() {
        UUID id = UUID.randomUUID();

        Ingresso ingresso1 = Ingresso.builder()
                .id(id)
                .qrHash("hash123")
                .status(IngressoStatus.VALIDO)
                .build();

        Ingresso ingresso2 = Ingresso.builder()
                .id(id)
                .qrHash("hash456")
                .status(IngressoStatus.UTILIZADO)
                .build();

        Ingresso ingressoDiferente = Ingresso.builder()
                .id(UUID.randomUUID())
                .qrHash("hash123")
                .build();

        assertThat(ingresso1).isEqualTo(ingresso2);
        assertThat(ingresso1).isNotEqualTo(ingressoDiferente);
        assertThat(ingresso1.hashCode()).isEqualTo(ingresso2.hashCode());
    }

    @Test
    @DisplayName("Deve instanciar a entidade Ingresso via Builder com todos os atributos")
    void deveInstanciarIngressoComSucesso() {
        UUID id = UUID.randomUUID();
        Reserva reserva = Reserva.builder().id(UUID.randomUUID()).build();

        Ingresso ingresso = Ingresso.builder()
                .id(id)
                .reserva(reserva)
                .qrHash("hash_hmac_123")
                .status(IngressoStatus.VALIDO)
                .shareToken("share_token_xyz")
                .build();

        assertThat(ingresso.getId()).isEqualTo(id);
        assertThat(ingresso.getReserva()).isEqualTo(reserva);
        assertThat(ingresso.getQrHash()).isEqualTo("hash_hmac_123");
        assertThat(ingresso.getStatus()).isEqualTo(IngressoStatus.VALIDO);
        assertThat(ingresso.getShareToken()).isEqualTo("share_token_xyz");
    }
}