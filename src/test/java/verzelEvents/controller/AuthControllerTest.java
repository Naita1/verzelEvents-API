package verzelEvents.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import verzelEvents.dto.request.CreateStaffRequest;
import verzelEvents.dto.request.LoginRequest;
import verzelEvents.dto.request.RegisterRequest;
import verzelEvents.dto.response.AuthResponse;
import verzelEvents.entity.RoleEnum;
import verzelEvents.service.AuthService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthService authService;

    @Test
    @DisplayName("Deve registrar cliente com sucesso e retornar status HTTP 201 Created")
    void deveRegistrarClienteComSucesso() throws Exception {
        RegisterRequest request = new RegisterRequest("Cliente Teste", "cliente@verzel.com", "senha123");
        AuthResponse response = new AuthResponse("token_jwt_valido", "Cliente Teste", "cliente@verzel.com", RoleEnum.CLIENTE);

        when(authService.register(any(RegisterRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").value("token_jwt_valido"))
                .andExpect(jsonPath("$.nome").value("Cliente Teste"))
                .andExpect(jsonPath("$.email").value("cliente@verzel.com"));
    }

    @Test
    @DisplayName("Deve realizar login com sucesso e retornar status HTTP 200 OK")
    void deveRealizarLoginComSucesso() throws Exception {
        LoginRequest request = new LoginRequest("cliente@verzel.com", "senha123");
        AuthResponse response = new AuthResponse("token_jwt_valido", "Cliente Teste", "cliente@verzel.com", RoleEnum.CLIENTE);

        when(authService.login(any(LoginRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("token_jwt_valido"));
    }

    @Test
    @DisplayName("Deve criar usuário Staff com sucesso e retornar status HTTP 201 Created")
    void deveCriarStaffComSucesso() throws Exception {
        CreateStaffRequest request = new CreateStaffRequest("Portaria Demo", "portaria@verzel.com", "senha123", RoleEnum.PORTARIA);
        AuthResponse response = new AuthResponse("token_jwt_valido", "Portaria Demo", "portaria@verzel.com", RoleEnum.PORTARIA);

        when(authService.criarStaff(any(CreateStaffRequest.class))).thenReturn(response);

        mockMvc.perform(post("/auth/staff")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.role").value("PORTARIA"));
    }
}