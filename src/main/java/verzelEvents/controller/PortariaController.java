package verzelEvents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Portaria", description = "Endpoints para validação de ingressos na entrada do evento")
public class PortariaController {

    private final PortariaService portariaService;

    @Operation(
            summary = "Validar um ingresso",
            description = "Recebe o código do QR Code de um ingresso e o ID do evento para validação. Retorna o resultado da validação (Válido, Inválido, Já Utilizado, etc.). Requer autenticação de um usuário 'PORTARIA'.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Validação processada. O corpo da resposta contém o resultado detalhado.",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ValidacaoResponse.class))),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos.", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado. Token JWT ausente ou inválido.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Apenas usuários da portaria podem validar ingressos.", content = @Content)
    })
    @PostMapping("/validar")
    public ResponseEntity<ValidacaoResponse> validateTicket(
            @Valid @RequestBody ValidarIngressoRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.ok(portariaService.validateTicket(request, authentication.getName()));
    }

    @Operation(
            summary = "Obter histórico de validações de um evento",
            description = "Retorna uma lista de todas as tentativas de validação (bem-sucedidas ou não) para um evento específico. Requer autenticação de um usuário 'PORTARIA'.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Histórico de validações recuperado com sucesso.",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = ValidacaoHistoryResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado. Token JWT ausente ou inválido.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Apenas usuários da portaria podem ver o histórico.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Evento não encontrado.", content = @Content)
    })
    @GetMapping("/eventos/{eventoId}/historico")
    public ResponseEntity<List<ValidacaoHistoryResponse>> getValidationHistory(@PathVariable UUID eventoId) {
        return ResponseEntity.ok(portariaService.getValidationHistory(eventoId));
    }
}