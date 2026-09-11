package verzelEvents.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando um usuário autenticado tenta realizar uma operação
 * ou acessar um recurso para o qual não possui permissão (role) adequada.
 */
public class ForbiddenOperationException extends BusinessException {
    public ForbiddenOperationException(String message) {
        super(message, HttpStatus.FORBIDDEN);
    }
}