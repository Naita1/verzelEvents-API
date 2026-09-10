package verzelEvents.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ReservaStatusTest {

    @Test
    @DisplayName("Deve conter exatamente os estados definidos para o ciclo de vida da reserva")
    void deveConterTodosOsEstados() {
        assertThat(ReservaStatus.values())
                .containsExactly(
                        ReservaStatus.PENDENTE,
                        ReservaStatus.CONFIRMADA,
                        ReservaStatus.EXPIRADA,
                        ReservaStatus.CANCELADA
                );
    }

    @Test
    @DisplayName("Deve converter String para ReservaStatus corretamente")
    void deveConverterFromString() {
        assertThat(ReservaStatus.valueOf("PENDENTE")).isEqualTo(ReservaStatus.PENDENTE);
        assertThat(ReservaStatus.valueOf("CONFIRMADA")).isEqualTo(ReservaStatus.CONFIRMADA);
        assertThat(ReservaStatus.valueOf("EXPIRADA")).isEqualTo(ReservaStatus.EXPIRADA);
        assertThat(ReservaStatus.valueOf("CANCELADA")).isEqualTo(ReservaStatus.CANCELADA);
    }
}