package verzelEvents.dto.request;

import verzelEvents.entity.RoleEnum;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class CreateStaffRequest {

    @NotBlank(message = "Nome é obrigatório")
    @Size(min = 2, max = 100, message = "O nome deve ter entre 2 e 100 caracteres")
    private String nome;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    @Size(max = 150, message = "O email não pode exceder 150 caracteres")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, max = 50, message = "A senha deve ter entre 6 e 50 caracteres")
    private String senha;

    @NotNull(message = "Role é obrigatória")
    private RoleEnum role; 
}