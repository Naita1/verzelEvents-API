package verzelEvents.exception;

import org.springframework.http.HttpStatus;

/**
 * Exceção lançada quando as credenciais de autenticação (e-mail e/ou senha)
 * fornecidas pelo usuário são inválidas ou não conferem durante o login.
 */
public class InvalidCredentialsException extends BusinessException {
    public InvalidCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED);
    }
}