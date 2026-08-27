package verzelEvents.controller;

import verzelEvents.dto.response.IngressoResponse;
import verzelEvents.service.IngressoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class IngressoController {

    private final IngressoService ingressoService;

    @GetMapping("/cliente/ingressos")
    public ResponseEntity<List<IngressoResponse>> getMyTickets(Authentication authentication) {
        return ResponseEntity.ok(ingressoService.getMyTickets(authentication.getName()));
    }

    @GetMapping("/tickets/share/{token}")
    public ResponseEntity<IngressoResponse> getSharedTicket(@PathVariable String token) {
        return ResponseEntity.ok(ingressoService.getSharedTicket(token));
    }
}