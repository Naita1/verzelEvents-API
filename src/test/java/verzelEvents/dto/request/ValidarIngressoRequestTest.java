package verzelEvents.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ValidarIngressoRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar com sucesso quando código e eventoId forem válidos")
    void deveValidarComSucesso() {
        ValidarIngressoRequest request = new ValidarIngressoRequest();
        request.setCodigo("UUID-INGRESSO-12345:HMAC_HASH_SIGNATURE");
        request.setEventoId(UUID.randomUUID());

        Set<ConstraintViolation<ValidarIngressoRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar se o código do QR Code ultrapassar 200 caracteres")
    void deveFalharComCodigoExcedendoLimite() {
        ValidarIngressoRequest request = new ValidarIngressoRequest();
        request.setCodigo("A".repeat(201));
        request.setEventoId(UUID.randomUUID());

        Set<ConstraintViolation<ValidarIngressoRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O código do ingresso não pode exceder 200 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Deve falhar quando o ID do evento ou o código forem nulos/em branco")
    void deveFalharComCamposNulos() {
        ValidarIngressoRequest request = new ValidarIngressoRequest();
        request.setCodigo("");
        request.setEventoId(null);

        Set<ConstraintViolation<ValidarIngressoRequest>> violations = validator.validate(request);

        assertEquals(2, violations.size());
    }
}