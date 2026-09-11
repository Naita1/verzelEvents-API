package verzelEvents.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import verzelEvents.entity.RoleEnum;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateStaffRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar com sucesso quando todos os dados do staff estão corretos")
    void deveValidarComSucesso() {
        CreateStaffRequest request = new CreateStaffRequest();
        request.setNome("Operador Silva");
        request.setEmail("portaria@verzel.com");
        request.setSenha("senhaSegura123");
        request.setRole(RoleEnum.PORTARIA);

        Set<ConstraintViolation<CreateStaffRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar se a senha tiver menos de 6 caracteres")
    void deveFalharComSenhaCurta() {
        CreateStaffRequest request = new CreateStaffRequest();
        request.setNome("Operador Silva");
        request.setEmail("portaria@verzel.com");
        request.setSenha("12345");
        request.setRole(RoleEnum.PORTARIA);

        Set<ConstraintViolation<CreateStaffRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("A senha deve ter entre 6 e 50 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Deve falhar se o e-mail for inválido ou o nome for muito curto")
    void deveFalharComEmailInvalidoENomeCurto() {
        CreateStaffRequest request = new CreateStaffRequest();
        request.setNome("A");
        request.setEmail("email_invalido");
        request.setSenha("senhaSegura123");
        request.setRole(RoleEnum.ORGANIZADOR);

        Set<ConstraintViolation<CreateStaffRequest>> violations = validator.validate(request);

        assertEquals(2, violations.size());
    }
}