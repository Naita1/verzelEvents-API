package verzelEvents.service;

import verzelEvents.dto.request.CreateStaffRequest;
import verzelEvents.dto.request.LoginRequest;
import verzelEvents.dto.request.RegisterRequest;
import verzelEvents.dto.response.AuthResponse;
import verzelEvents.entity.RoleEnum;
import verzelEvents.entity.Usuario;
import verzelEvents.repository.UsuarioRepository;
import verzelEvents.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthResponse register(RegisterRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .role(RoleEnum.CLIENTE)
                .ativo(true)
                .build();

        usuarioRepository.save(usuario);

        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token, usuario.getNome(), usuario.getRole());
    }

    public AuthResponse login(LoginRequest request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Email ou senha inválidos"));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenha())) {
            throw new IllegalArgumentException("Email ou senha inválidos");
        }

        String token = jwtService.generateToken(usuario);
        return new AuthResponse(token, usuario.getNome(), usuario.getRole());
    }

    public AuthResponse criarStaff(CreateStaffRequest request) {
        if (request.getRole() == RoleEnum.CLIENTE) {
            throw new IllegalArgumentException("Role inválida para criação de staff. Use /auth/register para cadastrar clientes.");
        }

        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado");
        }

        Usuario usuario = Usuario.builder()
                .nome(request.getNome())
                .email(request.getEmail())
                .senha(passwordEncoder.encode(request.getSenha()))
                .ativo(true)
                .build();

        usuarioRepository.save(usuario);

        return new AuthResponse(null, usuario.getNome(), usuario.getRole());
    }
}