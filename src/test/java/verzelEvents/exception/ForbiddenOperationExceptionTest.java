package verzelEvents.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ForbiddenOperationExceptionTest {

    @Test
    @DisplayName("Deve instanciar ForbiddenOperationException com status HTTP 403 FORBIDDEN")
    void deveInstanciarExcecaoComStatusForbidden() {
        String mensagem = "Usuário sem permissão para realizar esta operação";

        ForbiddenOperationException exception = new ForbiddenOperationException(mensagem);

        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.FORBIDDEN);
    }
}