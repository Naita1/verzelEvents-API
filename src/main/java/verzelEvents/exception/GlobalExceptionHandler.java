package verzelEvents.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Interceptador global de exceções.
 * Padroniza as respostas de erro da API para o formato JSON esperado pelo Front-end.
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @Getter
    @Setter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class ApiError {
        private final Instant timestamp;
        private final int status;
        private final String error;
        private final String message;
        private Map<String, String> fieldErrors;

        ApiError(HttpStatus status, String message) {
            this.timestamp = Instant.now();
            this.status = status.value();
            this.error = status.getReasonPhrase();
            this.message = message;
        }

        ApiError(HttpStatus status, String message, Map<String, String> fieldErrors) {
            this(status, message);
            this.fieldErrors = fieldErrors;
        }
    }

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiError> handleBusinessException(BusinessException ex) {
        ApiError apiError = new ApiError(ex.getStatus(), ex.getMessage());
        return new ResponseEntity<>(apiError, ex.getStatus());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });

        ApiError apiError = new ApiError(status, "Erro de validação nos campos informados.", errors);
        return new ResponseEntity<>(apiError, status);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleUnexpectedException(Exception ex) {
        log.error("Erro interno inesperado: ", ex);
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        ApiError apiError = new ApiError(status, "Ocorreu um erro interno inesperado no servidor.");
        return new ResponseEntity<>(apiError, status);
    }
}