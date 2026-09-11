package verzelEvents.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import verzelEvents.dto.request.ValidarIngressoRequest;
import verzelEvents.dto.response.ValidacaoHistoryResponse;
import verzelEvents.dto.response.ValidacaoResponse;
import verzelEvents.entity.ResultadoValidacao;
import verzelEvents.service.PortariaService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PortariaController.class)
@AutoConfigureMockMvc(addFilters = false)
class PortariaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PortariaService portariaService;

    @Test
    @DisplayName("Deve validar ingresso na portaria com sucesso e retornar HTTP 200 OK")
    @WithMockUser(username = "portaria@verzel.com", roles = "PORTARIA")
    void deveValidarIngressoComSucesso() throws Exception {
        ValidarIngressoRequest request = new ValidarIngressoRequest();

        ValidacaoResponse response = new ValidacaoResponse(
                "VALIDO",
                "Ingresso validado com sucesso!"
        );

        when(portariaService.validateTicket(any(ValidarIngressoRequest.class), eq("portaria@verzel.com")))
                .thenReturn(response);

        mockMvc.perform(post("/portaria/validar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.resultado").value("VALIDO"))
                .andExpect(jsonPath("$.mensagem").value("Ingresso validado com sucesso!"));
    }

    @Test
    @DisplayName("Deve obter histórico de validações de um evento com status HTTP 200 OK")
    @WithMockUser(username = "portaria@verzel.com", roles = "PORTARIA")
    void deveObterHistoricoDeValidacoesComSucesso() throws Exception {
        UUID eventoId = UUID.randomUUID();

        ValidacaoHistoryResponse historyItem = new ValidacaoHistoryResponse(
                LocalDateTime.now(),
                ResultadoValidacao.VALIDO,
                "Portaria Demo"
        );

        when(portariaService.getValidationHistory(eventoId)).thenReturn(List.of(historyItem));

        mockMvc.perform(get("/portaria/eventos/{eventoId}/historico", eventoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].portariaNome").value("Portaria Demo"))
                .andExpect(jsonPath("$[0].resultado").value("VALIDO"));
    }
}