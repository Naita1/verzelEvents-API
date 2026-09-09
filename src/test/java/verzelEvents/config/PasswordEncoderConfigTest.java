package verzelEvents.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;

class PasswordEncoderConfigTest {

    private final PasswordEncoderConfig config = new PasswordEncoderConfig();

    @Test
    @DisplayName("Deve instanciar BCryptPasswordEncoder com sucesso")
    void deveCriarBeanBCryptPasswordEncoder() {
        PasswordEncoder encoder = config.passwordEncoder();

        assertThat(encoder).isNotNull();
        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
    }

    @Test
    @DisplayName("Deve codificar e validar senhas corretamente")
    void deveCodificarEValidarSenha() {
        PasswordEncoder encoder = config.passwordEncoder();
        String rawPassword = "SenhaSegura123!";

        String encodedPassword = encoder.encode(rawPassword);

        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(encoder.matches(rawPassword, encodedPassword)).isTrue();
        assertThat(encoder.matches("SenhaIncorreta", encodedPassword)).isFalse();
    }
}