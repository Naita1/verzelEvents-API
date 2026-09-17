package verzelEvents.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.data.domain.PageImpl;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import verzelEvents.dto.request.CreateEventRequest;
import verzelEvents.dto.response.AssentoResponse;
import verzelEvents.dto.response.CatalogItemResponse;
import verzelEvents.dto.response.EventoResponse;
import verzelEvents.entity.AssentoStatus;
import verzelEvents.service.AssentoService;
import verzelEvents.service.EventoService;
import verzelEvents.service.TmdbService;

import java.math.BigDecimal;
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

@WebMvcTest(EventoController.class)
@AutoConfigureMockMvc(addFilters = false)
class EventoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private EventoService eventoService;

    @MockBean
    private TmdbService tmdbService;

    @MockBean
    private AssentoService assentoService;

    @Test
    @DisplayName("Deve listar eventos públicos com status HTTP 200 OK")
    void deveListarEventosPublicos() throws Exception {
        UUID eventoId = UUID.randomUUID();
        EventoResponse response = new EventoResponse(eventoId, "Matrix", "CINEMA", LocalDateTime.now(), "Sala 1", 100, new BigDecimal("35.00"), "PUBLICADO", null);

        when(eventoService.listEvents(any())).thenReturn(new PageImpl<>(List.of(response)));

        mockMvc.perform(get("/eventos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(eventoId.toString()))
                .andExpect(jsonPath("$[0].titulo").value("Matrix"));
    }

    @Test
    @DisplayName("Deve obter detalhes do evento com status HTTP 200 OK")
    void deveObterDetalhesDoEvento() throws Exception {
        UUID eventoId = UUID.randomUUID();
        EventoResponse response = new EventoResponse(eventoId, "Matrix", "CINEMA", LocalDateTime.now(), "Sala 1", 100, new BigDecimal("35.00"), "PUBLICADO", null);

        when(eventoService.getEventDetails(eventoId)).thenReturn(response);

        mockMvc.perform(get("/eventos/{id}", eventoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(eventoId.toString()))
                .andExpect(jsonPath("$.titulo").value("Matrix"));
    }

    @Test
    @DisplayName("Deve listar assentos do evento com status HTTP 200 OK")
    void deveListarAssentosDoEvento() throws Exception {
        UUID eventoId = UUID.randomUUID();
        UUID assentoId = UUID.randomUUID();
        AssentoResponse assento = new AssentoResponse(assentoId, "A1", AssentoStatus.LIVRE);

        when(assentoService.listByEvento(eventoId)).thenReturn(List.of(assento));

        mockMvc.perform(get("/eventos/{id}/assentos", eventoId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].codigo").value("A1"))
                .andExpect(jsonPath("$[0].status").value("LIVRE"));
    }

    @Test
    @DisplayName("Deve buscar catálogo do TMDb com status HTTP 200 OK")
    void deveBuscarCatalogoTmdb() throws Exception {
        CatalogItemResponse item = new CatalogItemResponse("The Matrix", "Sinopse...", "poster.jpg", "1999-03-31");

        when(tmdbService.searchMovies("Matrix")).thenReturn(List.of(item));

        mockMvc.perform(get("/organizador/eventos/catalogo").param("query", "Matrix"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("The Matrix"));
    }

    @Test
    @DisplayName("Deve criar evento e retornar status HTTP 201 Created")
    @WithMockUser(username = "organizador@verzel.com", roles = "ORGANIZADOR")
    void deveCriarEventoComSucesso() throws Exception {
        UUID eventoId = UUID.randomUUID();
        CreateEventRequest request = new CreateEventRequest();
        EventoResponse response = new EventoResponse(eventoId, "Matrix", "CINEMA", LocalDateTime.now().plusDays(5), "Sala 1", 50, new BigDecimal("40.00"), "PUBLICADO", null);

        when(eventoService.createEvent(any(CreateEventRequest.class), eq("organizador@verzel.com"))).thenReturn(response);

        mockMvc.perform(post("/organizador/eventos")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(eventoId.toString()))
                .andExpect(jsonPath("$.titulo").value("Matrix"));
    }
}