package verzelEvents.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando há tentativa de cadastro de um usuário com um e-mail já existente no sistema.
 */
public class EmailAlreadyExistsException extends BusinessException {
    public EmailAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
}