package verzelEvents.controller;

import verzelEvents.dto.request.ValidarIngressoRequest;
import verzelEvents.dto.response.ValidacaoHistoryResponse;
import verzelEvents.dto.response.ValidacaoResponse;
import verzelEvents.service.PortariaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/portaria")
@RequiredArgsConstructor
public class PortariaController {

    private final PortariaService portariaService;

    @PostMapping("/validar")
    public ResponseEntity<ValidacaoResponse> validateTicket(
            @Valid @RequestBody ValidarIngressoRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(portariaService.validateTicket(request, authentication.getName()));
    }

    @GetMapping("/eventos/{eventoId}/historico")
    public ResponseEntity<List<ValidacaoHistoryResponse>> getValidationHistory(@PathVariable UUID eventoId) {
        return ResponseEntity.ok(portariaService.getValidationHistory(eventoId));
    }
}