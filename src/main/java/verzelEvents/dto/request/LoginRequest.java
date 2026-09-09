package verzelEvents.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LoginRequest {

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 150, message = "O email não pode exceder 150 caracteres")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 50, message = "A senha deve ter entre 6 e 50 caracteres")
    private String senha;
}