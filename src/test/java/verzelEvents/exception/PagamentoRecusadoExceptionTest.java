package verzelEvents.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

class PagamentoRecusadoExceptionTest {

    @Test
    @DisplayName("Deve instanciar PagamentoRecusadoException com status HTTP 402 PAYMENT_REQUIRED")
    void deveInstanciarExcecaoComStatusPaymentRequired() {
        String mensagem = "Transação recusada pela operadora de cartão de crédito";

        PagamentoRecusadoException exception = new PagamentoRecusadoException(mensagem);

        assertThat(exception.getMessage()).isEqualTo(mensagem);
        assertThat(exception.getStatus()).isEqualTo(HttpStatus.PAYMENT_REQUIRED);
    }
}