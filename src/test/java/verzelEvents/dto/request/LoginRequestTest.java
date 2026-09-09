package verzelEvents.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LoginRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar com sucesso credenciais formatadas corretamente")
    void deveValidarComSucesso() {
        LoginRequest request = new LoginRequest();
        request.setEmail("usuario@verzel.com");
        request.setSenha("senhaSegura123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar ao receber e-mail malformado ou senha menor que 6 caracteres")
    void deveFalharComEmailInvalidoESenhaCurta() {
        LoginRequest request = new LoginRequest();
        request.setEmail("email_invalido");
        request.setSenha("123");

        Set<ConstraintViolation<LoginRequest>> violations = validator.validate(request);

        assertEquals(2, violations.size());
    }
}