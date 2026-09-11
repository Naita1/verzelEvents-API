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
import verzelEvents.dto.request.CreateReservaRequest;
import verzelEvents.dto.response.ReservaResponse;
import verzelEvents.service.ReservaService;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReservaService reservaService;

    @Test
    @DisplayName("Deve criar reserva com sucesso e retornar HTTP 201 Created")
    @WithMockUser(username = "cliente@verzel.com", roles = "CLIENTE")
    void deveCriarReservaComSucesso() throws Exception {
        UUID eventoId = UUID.randomUUID();
        UUID assentoId = UUID.randomUUID();
        UUID reservaId = UUID.randomUUID();

        CreateReservaRequest request = new CreateReservaRequest();
        request.setEventoId(eventoId);
        request.setAssentoId(assentoId);
        
        ReservaResponse response = new ReservaResponse(
                reservaId,
                "PENDENTE",
                LocalDateTime.now().plusMinutes(5),
                "Matrix Resurrections",
                "A1",
                "cliente@verzel.com"
        );

        when(reservaService.createReserva(any(CreateReservaRequest.class), eq("cliente@verzel.com")))
                .thenReturn(response);

        mockMvc.perform(post("/cliente/reservas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(reservaId.toString()))
                .andExpect(jsonPath("$.eventoTitulo").value("Matrix Resurrections"))
                .andExpect(jsonPath("$.assentoCodigo").value("A1"))
                .andExpect(jsonPath("$.status").value("PENDENTE"));
    }
}