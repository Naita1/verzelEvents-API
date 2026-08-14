package verzelEvents.dto.response;

import verzelEvents.entity.RoleEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String nome;
    private RoleEnum role;
}
