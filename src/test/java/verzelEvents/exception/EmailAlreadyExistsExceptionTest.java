package verzelEvents.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class EmailAlreadyExistsExceptionTest {

    @Test
    @DisplayName("Deve instanciar EmailAlreadyExistsException com status HTTP 409 CONFLICT")
    void deveInstanciarExcecaoComStatusConflict() {
        String mensagem = "E-mail já cadastrado no sistema";

        EmailAlreadyExistsException exception = new EmailAlreadyExistsException(mensagem);

        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
    }
}