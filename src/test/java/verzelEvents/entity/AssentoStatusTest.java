package verzelEvents.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class AssentoStatusTest {

    @Test
    @DisplayName("Deve conter exatamente os estados definidos para o ciclo de vida do assento")
    void deveConterTodosOsEstados() {
        assertThat(AssentoStatus.values())
                .containsExactly(AssentoStatus.LIVRE, AssentoStatus.RESERVADO, AssentoStatus.VENDIDO);
    }

    @Test
    @DisplayName("Deve converter corretamente a partir de String")
    void deveConverterFromString() {
        assertThat(AssentoStatus.valueOf("LIVRE")).isEqualTo(AssentoStatus.LIVRE);
        assertThat(AssentoStatus.valueOf("RESERVADO")).isEqualTo(AssentoStatus.RESERVADO);
        assertThat(AssentoStatus.valueOf("VENDIDO")).isEqualTo(AssentoStatus.VENDIDO);
    }
}