package verzelEvents.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import jakarta.persistence.EntityNotFoundException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class GlobalExceptionHandlerTest {

    private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

    @Test
    @DisplayName("Deve capturar BusinessException e retornar o status HTTP correspondente")
    void deveTratarBusinessException() {
        BusinessException ex = new ForbiddenOperationException("Acesso negado");

        ProblemDetail response = handler.handleBusinessException(ex);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.FORBIDDEN.value());
        assertThat(response.getDetail()).isEqualTo("Acesso negado");
    }

    @Test
    @DisplayName("Deve capturar EntityNotFoundException e retornar status HTTP 404")
    void deveTratarEntityNotFoundException() {
        EntityNotFoundException ex = new EntityNotFoundException("Recurso não encontrado");

        ProblemDetail response = handler.handleEntityNotFoundException(ex);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.NOT_FOUND.value());
        assertThat(response.getDetail()).isEqualTo("Recurso não encontrado");
    }

    @Test
    @DisplayName("Deve capturar MethodArgumentNotValidException e mapear os erros de validação dos campos")
    void deveTratarMethodArgumentNotValidException() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("dto", "email", "E-mail inválido");

        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getAllErrors()).thenReturn(List.of(fieldError));

        ProblemDetail response = handler.handleValidationExceptions(ex);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
        assertThat(response.getTitle()).isEqualTo("Dados inválidos");
        assertThat(response.getProperties()).containsKey("invalidParams");
    }

    @Test
    @DisplayName("Deve capturar exceções genéricas e retornar status HTTP 500 INTERNAL_SERVER_ERROR")
    void deveTratarExceptionGenerica() {
        Exception ex = new RuntimeException("Erro inesperado no banco de dados");

        ProblemDetail response = handler.handleUnexpectedException(ex);

        assertThat(response.getStatus()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
        assertThat(response.getDetail()).isEqualTo("Ocorreu um erro interno inesperado no servidor.");
    }
}