package verzelEvents.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import verzelEvents.dto.request.CreateStaffRequest;
import verzelEvents.dto.request.LoginRequest;
import verzelEvents.dto.request.RegisterRequest;
import verzelEvents.dto.response.AuthResponse;
import verzelEvents.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Autenticação", description = "Endpoints para registro e login de usuários")
public class AuthController {

    private final AuthService authService;

    @Operation(
            summary = "Registrar um novo cliente",
            description = "Cria um novo usuário com o papel de 'CLIENTE'. O e-mail deve ser único."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Cliente registrado com sucesso", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos (ex: e-mail inválido, senha curta)", content = @Content),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado no sistema", content = @Content)
    })
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Realizar login",
            description = "Autentica um usuário existente (cliente, organizador ou portaria) e retorna um token JWT para ser usado nas requisições protegidas."
    )
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login bem-sucedido", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas (e-mail ou senha incorretos)", content = @Content)
    })
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
            summary = "Criar um novo usuário Staff (Portaria)",
            description = "Cria um novo usuário com o papel de 'PORTARIA'. Este endpoint é protegido e requer autenticação de um usuário 'ORGANIZADOR'.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Usuário Staff criado com sucesso", content = { @Content(mediaType = "application/json", schema = @Schema(implementation = AuthResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Dados de entrada inválidos", content = @Content),
            @ApiResponse(responseCode = "401", description = "Não autenticado. Token JWT ausente ou inválido.", content = @Content),
            @ApiResponse(responseCode = "403", description = "Acesso negado. Apenas organizadores podem criar staff.", content = @Content),
            @ApiResponse(responseCode = "409", description = "E-mail já cadastrado no sistema", content = @Content)
    })
    @PostMapping("/staff")
    public ResponseEntity<AuthResponse> criarStaff(@Valid @RequestBody CreateStaffRequest request) {
        AuthResponse response = authService.criarStaff(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}