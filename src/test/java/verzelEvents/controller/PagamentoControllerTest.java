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
import verzelEvents.dto.request.PagamentoRequest;
import verzelEvents.dto.response.IngressoResponse;
import verzelEvents.service.PagamentoService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PagamentoController.class)
@AutoConfigureMockMvc(addFilters = false)
class PagamentoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PagamentoService pagamentoService;

    @Test
    @DisplayName("Deve processar pagamento de reserva com sucesso e retornar HTTP 200 OK")
    @WithMockUser(username = "cliente@verzel.com", roles = "CLIENTE")
    void deveProcessarPagamentoComSucesso() throws Exception {
        UUID reservaId = UUID.randomUUID();
        UUID ingressoId = UUID.randomUUID();
        PagamentoRequest request = new PagamentoRequest("1234567890123456", "12/28", "123", "CARTAO_CREDITO");
        IngressoResponse response = new IngressoResponse(
                ingressoId,
                "Matrix Resurrections",
                "A1",
                "cliente@verzel.com",
                "http://qr.code/link",
                "share_token_123",
                LocalDateTime.now()
        );

        when(pagamentoService.processPayment(eq(reservaId), any(PagamentoRequest.class), eq("cliente@verzel.com")))
                .thenReturn(response);

        mockMvc.perform(post("/cliente/reservas/{id}/pagamento", reservaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(ingressoId.toString()))
                .andExpect(jsonPath("$.eventoTitulo").value("Matrix Resurrections"))
                .andExpect(jsonPath("$.assentoCodigo").value("A1"));
    }
}