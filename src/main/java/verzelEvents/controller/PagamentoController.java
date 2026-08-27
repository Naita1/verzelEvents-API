package verzelEvents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Reservas e Pagamentos", description = "Endpoints para criar reservas de assentos e processar seus pagamentos.")
public class PagamentoController {

    private final PagamentoService pagamentoService;

    @Operation(
            summary = "Processar pagamento de uma reserva",
            description = "Simula o pagamento de uma reserva existente e, se bem-sucedido, converte a reserva em um ingresso. Requer autenticação de cliente.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Pagamento processado com sucesso e ingresso gerado",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IngressoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de pagamento inválidos ou reserva expirada", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado. Token JWT ausente ou inválido.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. A reserva não pertence ao usuário autenticado.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Reserva não encontrada.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito. A reserva já foi paga ou o assento não está mais disponível.", content = @Content)
    })
    @PostMapping("/{id}/pagamento")
    public ResponseEntity<IngressoResponse> processPayment(
            @PathVariable UUID id,
            @Valid @RequestBody PagamentoRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(pagamentoService.processPayment(id, request, authentication.getName()));
    }
}