package verzelEvents.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class ValidacaoTest {

    @Test
    @DisplayName("Deve garantir a igualdade entre registros de validação com base estritamente no ID")
    void deveTestarEqualsEHashCode() {
        UUID id = UUID.randomUUID();

        Validacao validacao1 = Validacao.builder()
                .id(id)
                .resultado(ResultadoValidacao.VALIDO)
                .build();

        Validacao validacao2 = Validacao.builder()
                .id(id)
                .resultado(ResultadoValidacao.JA_UTILIZADO)
                .build();

        Validacao validacaoDiferente = Validacao.builder()
                .id(UUID.randomUUID())
                .resultado(ResultadoValidacao.VALIDO)
                .build();

        assertThat(validacao1).isEqualTo(validacao2);
        assertThat(validacao1).isNotEqualTo(validacaoDiferente);
        assertThat(validacao1.hashCode()).isEqualTo(validacao2.hashCode());
    }

    @Test
    @DisplayName("Deve instanciar a entidade Validacao via Builder com todos os atributos")
    void deveInstanciarValidacaoComSucesso() {
        UUID id = UUID.randomUUID();
        LocalDateTime agora = LocalDateTime.now();
        Ingresso ingresso = Ingresso.builder().id(UUID.randomUUID()).build();
        Usuario portaria = Usuario.builder().id(UUID.randomUUID()).nome("Operador Portaria").build();

        Validacao validacao = Validacao.builder()
                .id(id)
                .ingresso(ingresso)
                .portaria(portaria)
                .resultado(ResultadoValidacao.VALIDO)
                .createdAt(agora)
                .build();

        assertThat(validacao.getId()).isEqualTo(id);
        assertThat(validacao.getIngresso()).isEqualTo(ingresso);
        assertThat(validacao.getPortaria().getNome()).isEqualTo("Operador Portaria");
        assertThat(validacao.getResultado()).isEqualTo(ResultadoValidacao.VALIDO);
        assertThat(validacao.getCreatedAt()).isEqualTo(agora);
    }
}