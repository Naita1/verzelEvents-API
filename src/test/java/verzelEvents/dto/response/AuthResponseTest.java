package verzelEvents.dto.response;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import verzelEvents.entity.RoleEnum;

import static org.assertj.core.api.Assertions.assertThat;

class AuthResponseTest {

    @Test
    @DisplayName("Deve instanciar o record AuthResponse e retornar os atributos corretamente")
    void deveInstanciarRecordComSucesso() {
        AuthResponse response = new AuthResponse("token_jwt_123", "Taina Ribeiro", RoleEnum.CLIENTE);

        assertThat(response.token()).isEqualTo("token_jwt_123");
        assertThat(response.nome()).isEqualTo("Taina Ribeiro");
        assertThat(response.role()).isEqualTo(RoleEnum.CLIENTE);
    }
}