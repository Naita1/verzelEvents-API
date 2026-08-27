package verzelEvents.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

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
}