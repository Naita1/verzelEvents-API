package verzelEvents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import verzelEvents.dto.response.IngressoResponse;
import verzelEvents.service.IngressoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Tag(name = "Ingressos", description = "Endpoints para visualização de ingressos")
public class IngressoController {

    private final IngressoService ingressoService;

    @Operation(
            summary = "Listar meus ingressos",
            description = "Retorna uma lista de todos os ingressos comprados pelo cliente autenticado.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de ingressos recuperada com sucesso",
                    content = @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = IngressoResponse.class)))),
            @ApiResponse(responseCode = "401", description = "Não autenticado. Token JWT ausente ou inválido.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Apenas clientes podem acessar seus ingressos.", content = @Content)
    })
    @GetMapping("/cliente/ingressos")
    public ResponseEntity<List<IngressoResponse>> getMyTickets(Authentication authentication) {
        return ResponseEntity.ok(ingressoService.getMyTickets(authentication.getName()));
    }

    @Operation(
            summary = "Visualizar ingresso compartilhado",
            description = "Retorna os detalhes de um ingresso a partir de um token de compartilhamento único. Este endpoint é público e não requer autenticação."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ingresso recuperado com sucesso",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = IngressoResponse.class))),
            @ApiResponse(responseCode = "404", description = "Ingresso não encontrado com o token fornecido.", content = @Content)
    })
    @GetMapping("/tickets/share/{token}")
    public ResponseEntity<IngressoResponse> getSharedTicket(@PathVariable String token) {
        return ResponseEntity.ok(ingressoService.getSharedTicket(token));
    }
}