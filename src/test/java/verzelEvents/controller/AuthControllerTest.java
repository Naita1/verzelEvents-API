package verzelEvents.controller;

import verzelEvents.dto.request.LoginRequest;
import verzelEvents.dto.response.AuthResponse;
import verzelEvents.entity.RoleEnum;
import verzelEvents.security.JwtService;
import verzelEvents.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
class AuthControllerTest {

    @Autowired private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();
    @MockitoBean private AuthService authService;
    @MockitoBean private JwtService jwtService;

    @Test
    @DisplayName("POST /auth/login deve retornar JWT e status 200 OK")
    void deveRealizarLoginComSucesso() throws Exception {
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setEmail("cliente1@verzel.com");
        loginRequest.setSenha("123456");

        AuthResponse authResponse = new AuthResponse("jwt-token-falso", "Cliente Um", RoleEnum.CLIENTE);
        when(authService.login(any(LoginRequest.class))).thenReturn(authResponse);

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token-falso"))
                .andExpect(jsonPath("$.role").value("CLIENTE"));
    }
}