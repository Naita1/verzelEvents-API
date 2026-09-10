package verzelEvents.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class AssentoTest {

    @Test
    @DisplayName("Deve garantir a igualdade entre entidades com base estritamente no ID")
    void deveTestarEqualsEHashCode() {
        UUID id = UUID.randomUUID();

        Assento assento1 = Assento.builder()
                .id(id)
                .codigo("A1")
                .status(AssentoStatus.LIVRE)
                .build();

        Assento assento2 = Assento.builder()
                .id(id)
                .codigo("A2")
                .status(AssentoStatus.RESERVADO)
                .build();

        Assento assentoDiferente = Assento.builder()
                .id(UUID.randomUUID())
                .codigo("A1")
                .status(AssentoStatus.LIVRE)
                .build();

        assertThat(assento1).isEqualTo(assento2);
        assertThat(assento1).isNotEqualTo(assentoDiferente);
        assertThat(assento1.hashCode()).isEqualTo(assento2.hashCode());
    }

    @Test
    @DisplayName("Deve instanciar a entidade Assento via Builder com todos os atributos")
    void deveInstanciarAssentoComSucesso() {
        Evento evento = Evento.builder().id(UUID.randomUUID()).titulo("Matrix").build();

        Assento assento = Assento.builder()
                .id(UUID.randomUUID())
                .evento(evento)
                .codigo("B10")
                .status(AssentoStatus.LIVRE)
                .version(0L)
                .build();

        assertThat(assento.getCodigo()).isEqualTo("B10");
        assertThat(assento.getStatus()).isEqualTo(AssentoStatus.LIVRE);
        assertThat(assento.getEvento().getTitulo()).isEqualTo("Matrix");
        assertThat(assento.getVersion()).isEqualTo(0L);
    }
}