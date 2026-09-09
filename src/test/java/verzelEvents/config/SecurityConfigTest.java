package verzelEvents.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import verzelEvents.security.JwtAuthFilter;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SecurityConfigTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private JwtAuthFilter jwtAuthFilter;

    @Test
    @DisplayName("Deve permitir acesso público aos endpoints mapeados no Swagger e rotas públicas")
    void devePermitirAcessoPublico() throws Exception {
        mockMvc.perform(get("/swagger-ui/index.html"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Deve negar acesso (401 Unauthorized) em rota protegida quando não autenticado")
    void deveRetornar401QuandoNaoAutenticado() throws Exception {
        mockMvc.perform(get("/cliente/ingressos"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("Deve negar acesso (403 Forbidden) quando o usuário não possui o perfil exigido")
    @WithMockUser(roles = "CLIENTE")
    void deveRetornar403QuandoRoleInvalida() throws Exception {
        mockMvc.perform(get("/organizador/dashboard"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Deve autorizar acesso quando o usuário possui a role correta para o recurso")
    @WithMockUser(roles = "ORGANIZADOR")
    void deveAutorizarAcessoComRoleCorreta() throws Exception {
        mockMvc.perform(get("/organizador/dashboard"))
                .andExpect(status().isNotFound()); // Ultrapassa a camada de segurança (não é 401/403)
    }

    @Test
    @DisplayName("Deve bloquear criação de staff para perfis que não sejam ORGANIZADOR")
    @WithMockUser(roles = "CLIENTE")
    void deveBloquearCriacaoDeStaffParaCliente() throws Exception {
        mockMvc.perform(post("/auth/staff"))
                .andExpect(status().isForbidden());
    }
}