package verzelEvents.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class ReservaExpiradaExceptionTest {

    @Test
    @DisplayName("Deve instanciar ReservaExpiradaException com status HTTP 410 GONE")
    void deveInstanciarExcecaoComStatusGone() {
        String mensagem = "A reserva expirou por ultrapassar o limite de tempo para pagamento";

        ReservaExpiradaException exception = new ReservaExpiradaException(mensagem);

        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.GONE);
    }
}