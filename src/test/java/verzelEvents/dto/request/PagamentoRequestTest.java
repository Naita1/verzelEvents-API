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

class PagamentoRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar com sucesso quando os dados do cartão respeitarem os limites")
    void deveValidarComSucesso() {
        PagamentoRequest request = new PagamentoRequest();
        request.setNumeroCartao("4532015893021948");
        request.setNomeCartao("Fulano de Tal");

        Set<ConstraintViolation<PagamentoRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar se o número do cartão for muito curto ou muito longo")
    void deveFalharComNumeroCartaoInvalido() {
        PagamentoRequest request = new PagamentoRequest();
        request.setNumeroCartao("1234"); // Menos de 13 dígitos
        request.setNomeCartao("Fulano de Tal");

        Set<ConstraintViolation<PagamentoRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O número do cartão deve ter entre 13 e 20 caracteres", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Deve falhar quando o nome no cartão for em branco ou muito curto")
    void deveFalharComNomeCartaoInvalido() {
        PagamentoRequest request = new PagamentoRequest();
        request.setNumeroCartao("4532015893021948");
        request.setNomeCartao("A");

        Set<ConstraintViolation<PagamentoRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O nome no cartão deve ter entre 2 e 100 caracteres", violations.iterator().next().getMessage());
    }
}