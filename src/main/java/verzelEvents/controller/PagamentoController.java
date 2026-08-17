package verzelEvents.controller;

import verzelEvents.dto.request.PagamentoRequest;
import verzelEvents.dto.response.IngressoResponse;
import verzelEvents.service.PagamentoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/cliente/reservas")
@RequiredArgsConstructor
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @PostMapping("/{id}/pagamento")
    public ResponseEntity<IngressoResponse> processPayment(
            @PathVariable UUID id,
            @Valid @RequestBody PagamentoRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(pagamentoService.processPayment(id, request, authentication.getName()));
    }
}