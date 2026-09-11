package verzelEvents.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando um recurso solicitado (como um Evento, Usuário ou Reserva)
 * não é encontrado no banco de dados. Utiliza o status HTTP 404 NOT_FOUND.
 */
public class ResourceNotFoundException extends BusinessException {
    public ResourceNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND);
    }
}