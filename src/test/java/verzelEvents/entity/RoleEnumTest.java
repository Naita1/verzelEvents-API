package verzelEvents.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RoleEnumTest {

    @Test
    @DisplayName("Deve conter exatamente todas as roles de acesso do sistema")
    void deveConterTodasAsRoles() {
        assertThat(RoleEnum.values())
                .containsExactly(RoleEnum.CLIENTE, RoleEnum.ORGANIZADOR, RoleEnum.PORTARIA);
    }

    @Test
    @DisplayName("Deve converter String para RoleEnum corretamente")
    void deveConverterFromString() {
        assertThat(RoleEnum.valueOf("CLIENTE")).isEqualTo(RoleEnum.CLIENTE);
        assertThat(RoleEnum.valueOf("ORGANIZADOR")).isEqualTo(RoleEnum.ORGANIZADOR);
        assertThat(RoleEnum.valueOf("PORTARIA")).isEqualTo(RoleEnum.PORTARIA);
    }
}