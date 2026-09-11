package verzelEvents.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando há uma tentativa de reserva de um assento que já se encontra
 * ocupado ou pendente de pagamento por outro usuário. Utiliza o status HTTP 409 CONFLICT.
 */
public class SeatAlreadyReservedException extends BusinessException {
    public SeatAlreadyReservedException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}