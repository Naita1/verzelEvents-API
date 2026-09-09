package verzelEvents.dto.response;

import verzelEvents.entity.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Resposta contendo o token JWT e as informações básicas do usuário autenticado")
public record AuthResponse(
        @Schema(description = "Token JWT para autenticação nas rotas protegidas", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String token,
        @Schema(description = "Nome do usuário autenticado", example = "Taina Ribeiro") String nome,
        @Schema(description = "Papel (Role) do usuário no sistema") RoleEnum role
) {}
