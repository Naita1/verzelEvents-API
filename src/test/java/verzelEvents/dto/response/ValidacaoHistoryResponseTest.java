package verzelEvents.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import verzelEvents.entity.ResultadoValidacao;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class ValidacaoHistoryResponseTest {

    @Test
    @DisplayName("Deve instanciar o record ValidacaoHistoryResponse e retornar os atributos de auditoria corretamente")
    void deveInstanciarRecordComSucesso() {
        LocalDateTime agora = LocalDateTime.now();
        ValidacaoHistoryResponse response = new ValidacaoHistoryResponse(
                agora,
                ResultadoValidacao.VALIDO,
                "Portaria Demo"
        );

        assertThat(response.dataHora()).isEqualTo(agora);
        assertThat(response.resultado()).isEqualTo(ResultadoValidacao.VALIDO);
        assertThat(response.portariaNome()).isEqualTo("Portaria Demo");
    }
}