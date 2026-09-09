package verzelEvents.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ValidacaoResponseTest {

    @Test
    @DisplayName("Deve instanciar o record ValidacaoResponse e retornar os atributos do resultado da validação")
    void deveInstanciarRecordComSucesso() {
        ValidacaoResponse response = new ValidacaoResponse("VALIDO", "Ingresso validado com sucesso!");

        assertThat(response.resultado()).isEqualTo("VALIDO");
        assertThat(response.mensagem()).isEqualTo("Ingresso validado com sucesso!");
    }
}