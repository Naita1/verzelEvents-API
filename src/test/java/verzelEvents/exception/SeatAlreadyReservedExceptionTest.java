package verzelEvents.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class SeatAlreadyReservedExceptionTest {

    @Test
    @DisplayName("Deve instanciar SeatAlreadyReservedException com status HTTP 409 CONFLICT")
    void deveInstanciarExcecaoComStatusConflict() {
        String mensagem = "Assento já reservado";

        SeatAlreadyReservedException exception = new SeatAlreadyReservedException(mensagem);

        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.CONFLICT);
    }
}