package verzelEvents.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando a tentativa de pagamento de uma reserva falha ou é recusada.
 * Utiliza o semântico status HTTP 402 PAYMENT_REQUIRED.
 */
public class PagamentoRecusadoException extends BusinessException {
    public PagamentoRecusadoException(String message) {
        super(message, HttpStatus.PAYMENT_REQUIRED);
    }
}