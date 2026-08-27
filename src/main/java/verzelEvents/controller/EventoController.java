package verzelEvents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import verzelEvents.dto.request.CreateEventRequest;
import verzelEvents.dto.response.AssentoResponse;
import verzelEvents.dto.response.CatalogItemResponse;
import verzelEvents.dto.response.EventoResponse;
import verzelEvents.service.AssentoService;
import verzelEvents.service.EventoService;
import verzelEvents.service.TmdbService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Tag(name = "Eventos", description = "Endpoints para visualização e gerenciamento de eventos")
public class EventoController {

    private final EventoService eventoService;
    private final TmdbService tmdbService;
    private final AssentoService assentoService;

    @Operation(
            summary = "Listar todos os eventos publicados",
            description = "Retorna uma lista de todos os eventos que estão com o status 'PUBLICADO'. Este endpoint é público."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de eventos recuperada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventoResponse.class))))
    })
    @GetMapping("/eventos")
    public ResponseEntity<List<EventoResponse>> listEvents() {
        return ResponseEntity.ok(eventoService.listEvents());
    }

    @Operation(
            summary = "Obter detalhes de um evento",
            description = "Busca e retorna os detalhes de um evento específico pelo seu ID. Este endpoint é público."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Detalhes do evento recuperados com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado", content = @Content)
    })
    @GetMapping("/eventos/{id}")
    public ResponseEntity<EventoResponse> getEventDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(eventoService.getEventDetails(id));
    }

    @Operation(
            summary = "Listar assentos de um evento",
            description = "Retorna a lista de todos os assentos para um evento específico, com seu status (LIVRE, RESERVADO, VENDIDO). Este endpoint é público."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de assentos recuperada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = AssentoResponse.class)))),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado", content = @Content)
    })
    @GetMapping("/eventos/{id}/assentos")
    public ResponseEntity<List<AssentoResponse>> getAssentosByEvento(@PathVariable UUID id) {
        return ResponseEntity.ok(assentoService.listByEvento(id));
    }

    @Operation(
            summary = "Buscar no catálogo externo (TMDb)",
            description = "Permite que um organizador autenticado busque por filmes no catálogo do TMDb para usar como base para um novo evento.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Busca no catálogo realizada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = CatalogItemResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado. Token JWT ausente ou inválido.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Apenas organizadores podem acessar este recurso.", content = @Content)
    })
    @GetMapping("/organizador/eventos/catalogo")
    public ResponseEntity<List<CatalogItemResponse>> searchCatalog(@RequestParam String query) {
        return ResponseEntity.ok(tmdbService.searchMovies(query));
    }

    @Operation(
            summary = "Criar um novo evento",
            description = "Cria um novo evento a partir dos dados fornecidos. Requer autenticação de um usuário 'ORGANIZADOR'.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Evento criado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EventoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado. Token JWT ausente ou inválido.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Apenas organizadores podem criar eventos.", content = @Content)
    })
    @PostMapping("/organizador/eventos")
    public ResponseEntity<EventoResponse> createEvent(
            @Valid @RequestBody CreateEventRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(eventoService.createEvent(request, authentication.getName()));
    }

    @Operation(
            summary = "Listar eventos do organizador",
            description = "Retorna uma lista de todos os eventos criados pelo organizador autenticado.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de eventos do organizador recuperada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EventoResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado. Token JWT ausente ou inválido.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Apenas organizadores podem listar seus eventos.", content = @Content)
    })
    @GetMapping("/organizador/eventos")
    public ResponseEntity<List<EventoResponse>> listMyEvents(Authentication authentication) {
        return ResponseEntity.ok(eventoService.listMyEvents(authentication.getName()));
    }
}