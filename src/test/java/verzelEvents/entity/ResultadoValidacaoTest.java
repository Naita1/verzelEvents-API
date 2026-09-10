package verzelEvents.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResultadoValidacaoTest {

    @Test
    @DisplayName("Deve conter exatamente todos os resultados de validação de portaria previstos")
    void deveConterTodosOsResultados() {
        assertThat(ResultadoValidacao.values())
                .containsExactly(
                        ResultadoValidacao.VALIDO,
                        ResultadoValidacao.INVALIDO,
                        ResultadoValidacao.JA_UTILIZADO,
                        ResultadoValidacao.EVENTO_ERRADO
                );
    }

    @Test
    @DisplayName("Deve converter String para ResultadoValidacao corretamente")
    void deveConverterFromString() {
        assertThat(ResultadoValidacao.valueOf("VALIDO")).isEqualTo(ResultadoValidacao.VALIDO);
        assertThat(ResultadoValidacao.valueOf("INVALIDO")).isEqualTo(ResultadoValidacao.INVALIDO);
        assertThat(ResultadoValidacao.valueOf("JA_UTILIZADO")).isEqualTo(ResultadoValidacao.JA_UTILIZADO);
        assertThat(ResultadoValidacao.valueOf("EVENTO_ERRADO")).isEqualTo(ResultadoValidacao.EVENTO_ERRADO);
    }
}