package verzelEvents.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção genérica lançada quando uma operação solicitada viola as regras de negócio
 * ou quando o estado atual do sistema (ou da entidade) não permite sua execução.
 */
public class InvalidOperationException extends BusinessException {
    public InvalidOperationException(String message) {
        super(message, HttpStatus.BAD_REQUEST);
    }
}