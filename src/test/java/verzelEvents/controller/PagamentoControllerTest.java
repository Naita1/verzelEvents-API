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
        
        PagamentoRequest request = new PagamentoRequest();
        request.setNumeroCartao("1234567890123456");
        request.setNomeCartao("Cartao Teste");        
        IngressoResponse response = new IngressoResponse(
                ingressoId,
                "VALIDO",
                "hash_qr_code_123",
                "share_token_123",
                "Matrix Resurrections",
                "A1",
                "reserva_123:hash_qr_code_123"
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