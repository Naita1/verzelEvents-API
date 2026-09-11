package verzelEvents.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import verzelEvents.dto.response.IngressoResponse;
import verzelEvents.service.IngressoService;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IngressoController.class)
@AutoConfigureMockMvc(addFilters = false)
class IngressoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IngressoService ingressoService;

    @Test
    @DisplayName("Deve listar os ingressos do cliente autenticado com status HTTP 200 OK")
    @WithMockUser(username = "cliente@verzel.com", roles = "CLIENTE")
    void deveListarMeusIngressosComSucesso() throws Exception {
        UUID ingressoId = UUID.randomUUID();
        IngressoResponse response = new IngressoResponse(
                ingressoId,
                "VALIDO",
                "hash_qr_code_123",
                "share_token_123",
                "Matrix Resurrections",
                "A1",
                "reserva_123:hash_qr_code_123"
        );

        when(ingressoService.getMyTickets("cliente@verzel.com")).thenReturn(List.of(response));

        mockMvc.perform(get("/cliente/ingressos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(ingressoId.toString()))
                .andExpect(jsonPath("$[0].eventoTitulo").value("Matrix Resurrections"))
                .andExpect(jsonPath("$[0].assentoCodigo").value("A1"));
    }

    @Test
    @DisplayName("Deve obter ingresso compartilhado via token público com status HTTP 200 OK")
    void deveObterIngressoCompartilhadoComSucesso() throws Exception {
        UUID ingressoId = UUID.randomUUID();
        String token = "share_token_123";
        IngressoResponse response = new IngressoResponse(
                ingressoId,
                "VALIDO",
                "hash_qr_code_123",
                token,
                "Matrix Resurrections",
                "A1",
                "reserva_123:hash_qr_code_123"
        );

        when(ingressoService.getSharedTicket(eq(token))).thenReturn(response);

        mockMvc.perform(get("/tickets/share/{token}", token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ingressoId.toString()))
                .andExpect(jsonPath("$.shareToken").value(token));
    }
}