package verzelEvents.exception;

import org.springframework.http.HttpStatus;

public class ReservaExpiradaException extends BusinessException {
    public ReservaExpiradaException(String message) {
        super(message, HttpStatus.GONE);
    }
}