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
        UUID eventoId = UUID.randomUUID();
        ValidarIngressoRequest request = new ValidarIngressoRequest(eventoId, "QR_CODE_HMAC_HASH");
        ValidacaoResponse response = new ValidacaoResponse(true, "ENTRADA_LIBERADA", "Ingresso válido. Acesso permitido.", LocalDateTime.now());

        when(portariaService.validateTicket(any(ValidarIngressoRequest.class), eq("portaria@verzel.com")))
                .thenReturn(response);

        mockMvc.perform(post("/portaria/validar")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.valido").value(true))
                .andExpect(jsonPath("$.status").value("ENTRADA_LIBERADA"));
    }

    @Test
    @DisplayName("Deve obter histórico de validações de um evento com status HTTP 200 OK")
    @WithMockUser(username = "portaria@verzel.com", roles = "PORTARIA")
    void deveObterHistoricoDeValidacoesComSucesso() throws Exception {
        UUID eventoId = UUID.randomUUID();
        ValidacaoHistoryResponse historyItem = new ValidacaoHistoryResponse(
                UUID.randomUUID(),
                "Cliente Um",
                "A1",
                true,
                "ENTRADA_LIBERADA",
                LocalDateTime.now()
        );

        when(portariaService.getValidationHistory(eventoId)).thenReturn(List.of(historyItem));

        mockMvc.perform(get("/portaria/eventos/{eventoId}/historico", eventoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nomeCliente").value("Cliente Um"))
                .andExpect(jsonPath("$[0].assentoCodigo").value("A1"))
                .andExpect(jsonPath("$[0].valido").value(true));
    }
}