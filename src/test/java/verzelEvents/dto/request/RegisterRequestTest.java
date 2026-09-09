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

class RegisterRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar com sucesso dados de registro bem formatados")
    void deveValidarComSucesso() {
        RegisterRequest request = new RegisterRequest();
        request.setNome("Maria Silva");
        request.setEmail("maria@verzel.com");
        request.setSenha("senhaSegura123");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar se a senha for muito curta ou o nome tiver menos de 2 caracteres")
    void deveFalharComSenhaCurtaENomeInvalido() {
        RegisterRequest request = new RegisterRequest();
        request.setNome("M");
        request.setEmail("maria@verzel.com");
        request.setSenha("12345");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertEquals(2, violations.size());
    }

    @Test
    @DisplayName("Deve falhar ao tentar registrar com e-mail inválido")
    void deveFalharComEmailInvalido() {
        RegisterRequest request = new RegisterRequest();
        request.setNome("Maria Silva");
        request.setEmail("email_invalido");
        request.setSenha("senhaSegura123");

        Set<ConstraintViolation<RegisterRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("Email inválido", violations.iterator().next().getMessage());
    }
}