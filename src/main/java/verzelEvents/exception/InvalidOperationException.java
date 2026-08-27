package verzelEvents.exception;

import org.springframework.http.HttpStatus;

public class InvalidOperationException extends BusinessException {
    public InvalidOperationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}