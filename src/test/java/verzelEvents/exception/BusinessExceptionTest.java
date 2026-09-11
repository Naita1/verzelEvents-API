package verzelEvents.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class BusinessExceptionTest {

    private static class TesteExcecaoRegraNegocio extends BusinessException {
        public TesteExcecaoRegraNegocio(String message, HttpStatus status) {
            super(message, status);
        }
    }

    @Test
    @DisplayName("Deve reter a mensagem de erro e o HttpStatus informados na exceção de negócio")
    void deveCriarExcecaoComMensagemEStatus() {
        String mensagem = "Assento temporariamente indisponível";
        HttpStatus status = HttpStatus.CONFLICT;

        BusinessException exception = new TesteExcecaoRegraNegocio(mensagem, status);

        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getStatus()).isEqualTo(status);
    }
}