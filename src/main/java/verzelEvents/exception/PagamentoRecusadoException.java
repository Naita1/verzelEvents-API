package verzelEvents.exception;

import org.springframework.http.HttpStatus;

public class PagamentoRecusadoException extends BusinessException {
    public PagamentoRecusadoException(String message) {
        super(message, HttpStatus.PAYMENT_REQUIRED);
    }
}