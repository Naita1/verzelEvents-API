package verzelEvents.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ResourceNotFoundExceptionTest {

    @Test
    @DisplayName("Deve instanciar ResourceNotFoundException com status HTTP 404 NOT_FOUND")
    void deveInstanciarExcecaoComStatusNotFound() {
        String mensagem = "Evento não encontrado com o ID informado";

        ResourceNotFoundException exception = new ResourceNotFoundException(mensagem);

        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}