package verzelEvents.controller;

import verzelEvents.dto.request.CreateEventRequest;
import verzelEvents.dto.response.CatalogItemResponse;
import verzelEvents.dto.response.EventoResponse;
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
public class EventoController {

    private final EventoService eventoService;
    private final TmdbService tmdbService;

    @GetMapping("/eventos")
    public ResponseEntity<List<EventoResponse>> listEvents() {
        return ResponseEntity.ok(eventoService.listEvents());
    }

    @GetMapping("/eventos/{id}")
    public ResponseEntity<EventoResponse> getEventDetails(@PathVariable UUID id) {
        return ResponseEntity.ok(eventoService.getEventDetails(id));
    }

    @GetMapping("/organizador/eventos/catalogo")
    public ResponseEntity<List<CatalogItemResponse>> searchCatalog(@RequestParam String query) {
        return ResponseEntity.ok(tmdbService.searchMovies(query));
    }

    @PostMapping("/organizador/eventos")
    public ResponseEntity<EventoResponse> createEvent(
            @Valid @RequestBody CreateEventRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(eventoService.createEvent(request, authentication.getName()));
    }

    @GetMapping("/organizador/eventos")
    public ResponseEntity<List<EventoResponse>> listMyEvents(Authentication authentication) {
        return ResponseEntity.ok(eventoService.listMyEvents(authentication.getName()));
    }
}