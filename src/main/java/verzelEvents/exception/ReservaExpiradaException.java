package verzelEvents.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando uma tentativa de pagamento ou acesso é feita em uma reserva
 * que já ultrapassou o tempo limite e expirou. Utiliza o semântico status HTTP 410 GONE.
 */
public class ReservaExpiradaException extends BusinessException {
    public ReservaExpiradaException(String message) {
        super(message, HttpStatus.GONE);
    }
}