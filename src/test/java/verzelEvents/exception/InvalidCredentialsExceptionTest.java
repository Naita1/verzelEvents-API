package verzelEvents.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidCredentialsExceptionTest {

    @Test
    @DisplayName("Deve instanciar InvalidCredentialsException com status HTTP 401 UNAUTHORIZED")
    void deveInstanciarExcecaoComStatusUnauthorized() {
        String mensagem = "E-mail ou senha inválidos";

        InvalidCredentialsException exception = new InvalidCredentialsException(mensagem);

        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }
}