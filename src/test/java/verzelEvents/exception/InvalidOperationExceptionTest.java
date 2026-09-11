package verzelEvents.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class InvalidOperationExceptionTest {

    @Test
    @DisplayName("Deve instanciar InvalidOperationException com status HTTP 400 BAD_REQUEST")
    void deveInstanciarExcecaoComStatusBadRequest() {
        String mensagem = "Operação inválida para o estado atual da reserva";

        InvalidOperationException exception = new InvalidOperationException(mensagem);

        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST);
    }
}