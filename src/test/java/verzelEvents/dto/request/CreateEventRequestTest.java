package verzelEvents.dto.request;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CreateEventRequestTest {

    private static Validator validator;

    @BeforeAll
    static void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    @DisplayName("Deve validar com sucesso quando todos os campos estão corretos")
    void deveValidarComSucesso() {
        CreateEventRequest request = new CreateEventRequest();
        request.setTitulo("Matrix Resurrections");
        request.setTipo("CINEMA");
        request.setDataHora(LocalDateTime.now().plusDays(10));
        request.setLocal("Cinema Central");
        request.setCapacidade(100);
        request.setPreco(new BigDecimal("45.00"));

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);

        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Deve falhar ao tentar criar evento com data no passado")
    void deveFalharComDataNoPassado() {
        CreateEventRequest request = new CreateEventRequest();
        request.setTitulo("Matrix");
        request.setTipo("CINEMA");
        request.setDataHora(LocalDateTime.now().minusDays(1));
        request.setLocal("Cinema Central");
        request.setCapacidade(100);
        request.setPreco(new BigDecimal("45.00"));

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("A data e hora do evento devem ser no futuro", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Deve falhar quando a capacidade ultrapassar o limite máximo de 10.000 assentos")
    void deveFalharComCapacidadeExcedida() {
        CreateEventRequest request = new CreateEventRequest();
        request.setTitulo("Matrix");
        request.setTipo("CINEMA");
        request.setDataHora(LocalDateTime.now().plusDays(5));
        request.setLocal("Estádio");
        request.setCapacidade(10001);
        request.setPreco(new BigDecimal("45.00"));

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("A capacidade máxima permitida é de 10.000 assentos", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Deve falhar se o preço tiver precisão numérica incorreta")
    void deveFalharComPrecoInvalido() {
        CreateEventRequest request = new CreateEventRequest();
        request.setTitulo("Matrix");
        request.setTipo("CINEMA");
        request.setDataHora(LocalDateTime.now().plusDays(5));
        request.setLocal("Cinema Central");
        request.setCapacidade(100);
        request.setPreco(new BigDecimal("123456789.999")); 

        Set<ConstraintViolation<CreateEventRequest>> violations = validator.validate(request);

        assertEquals(1, violations.size());
        assertEquals("O preço deve ter no máximo 8 dígitos inteiros e 2 decimais", violations.iterator().next().getMessage());
    }
}