package verzelEvents.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class UsuarioTest {

    @Test
    @DisplayName("Deve garantir a igualdade entre usuários com base estritamente no ID")
    void deveTestarEqualsEHashCode() {
        UUID id = UUID.randomUUID();

        Usuario usuario1 = Usuario.builder()
                .id(id)
                .email("taina@verzel.com")
                .role(RoleEnum.CLIENTE)
                .build();

        Usuario usuario2 = Usuario.builder()
                .id(id)
                .email("outro@verzel.com")
                .role(RoleEnum.ORGANIZADOR)
                .build();

        Usuario usuarioDiferente = Usuario.builder()
                .id(UUID.randomUUID())
                .email("taina@verzel.com")
                .build();

        assertThat(usuario1).isEqualTo(usuario2);
        assertThat(usuario1).isNotEqualTo(usuarioDiferente);
        assertThat(usuario1.hashCode()).isEqualTo(usuario2.hashCode());
    }

    @Test
    @DisplayName("Deve retornar as authorities e contratos do UserDetails corretamente")
    void deveTestarContratosUserDetails() {
        Usuario usuario = Usuario.builder()
                .id(UUID.randomUUID())
                .nome("Taina Ribeiro")
                .email("taina@verzel.com")
                .senha("hash_senha_bcrypt")
                .role(RoleEnum.ORGANIZADOR)
                .ativo(true)
                .build();

        Collection<? extends GrantedAuthority> authorities = usuario.getAuthorities();

        assertThat(authorities).extracting(GrantedAuthority::getAuthority)
                .containsExactly("ROLE_ORGANIZADOR");
        assertThat(usuario.getUsername()).isEqualTo("taina@verzel.com");
        assertThat(usuario.getPassword()).isEqualTo("hash_senha_bcrypt");
        assertThat(usuario.isEnabled()).isTrue();
        assertThat(usuario.isAccountNonExpired()).isTrue();
        assertThat(usuario.isAccountNonLocked()).isTrue();
        assertThat(usuario.isCredentialsNonExpired()).isTrue();
    }
}