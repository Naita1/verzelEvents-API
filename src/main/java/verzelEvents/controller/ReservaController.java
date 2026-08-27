package verzelEvents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import verzelEvents.dto.request.CreateReservaRequest;
import verzelEvents.dto.response.ReservaResponse;
import verzelEvents.service.ReservaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cliente/reservas")
@RequiredArgsConstructor
@Tag(name = "Reservas e Pagamentos", description = "Endpoints para criar reservas de assentos e processar seus pagamentos.")
public class ReservaController {

    private final ReservaService reservaService;

    @Operation(
            summary = "Criar uma reserva de assento",
            description = "Cria uma reserva temporária (válida por 5 minutos) para um assento em um evento específico. Requer autenticação de cliente.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Reserva criada com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReservaResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado. Token JWT ausente ou inválido.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Apenas clientes podem criar reservas.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Evento ou assento não encontrado.", content = @Content),
            @ApiResponse(responseCode = "409", description = "Conflito. O assento selecionado já está ocupado ou reservado.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<ReservaResponse> createReserva(
            @Valid @RequestBody CreateReservaRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(reservaService.createReserva(request, authentication.getName()));
    }
}