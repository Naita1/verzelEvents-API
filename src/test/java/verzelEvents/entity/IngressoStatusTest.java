package verzelEvents.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class IngressoStatusTest {

    @Test
    @DisplayName("Deve conter exatamente os estados previstos para o ciclo de vida do ingresso")
    void deveConterTodosOsEstados() {
        assertThat(IngressoStatus.values())
                .containsExactly(IngressoStatus.EMITIDO, IngressoStatus.VALIDADO, IngressoStatus.CANCELADO);
    }

    @Test
    @DisplayName("Deve converter String para IngressoStatus corretamente")
    void deveConverterFromString() {
        assertThat(IngressoStatus.valueOf("EMITIDO")).isEqualTo(IngressoStatus.EMITIDO);
        assertThat(IngressoStatus.valueOf("VALIDADO")).isEqualTo(IngressoStatus.VALIDADO);
        assertThat(IngressoStatus.valueOf("CANCELADO")).isEqualTo(IngressoStatus.CANCELADO);
    }
}