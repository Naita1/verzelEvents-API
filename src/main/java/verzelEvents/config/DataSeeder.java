package verzelEvents.config;

import verzelEvents.entity.*;
import verzelEvents.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;
    private final AssentoRepository assentoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            return; 
        }

        Usuario organizador = usuarioRepository.save(Usuario.builder()
                .nome("Organizador Demo")
                .email("organizador@verzel.com")
                .senha(passwordEncoder.encode("123456"))
                .role(RoleEnum.ORGANIZADOR)
                .ativo(true)
                .build());

        usuarioRepository.save(Usuario.builder()
                .nome("Cliente Um")
                .email("cliente1@verzel.com")
                .senha(passwordEncoder.encode("123456"))
                .role(RoleEnum.CLIENTE)
                .ativo(true)
                .build());

        usuarioRepository.save(Usuario.builder()
                .nome("Cliente Dois")
                .email("cliente2@verzel.com")
                .senha(passwordEncoder.encode("123456"))
                .role(RoleEnum.CLIENTE)
                .ativo(true)
                .build());

        usuarioRepository.save(Usuario.builder()
                .nome("Portaria Demo")
                .email("portaria@verzel.com")
                .senha(passwordEncoder.encode("123456"))
                .role(RoleEnum.PORTARIA)
                .ativo(true)
                .build());

        Evento evento = eventoRepository.save(Evento.builder()
                .organizador(organizador)
                .titulo("Matrix Resurrections")
                .tipo("Cinema")
                .dataHora(LocalDateTime.now().plusDays(7))
                .local("Sala 1 - Cine Verzel")
                .capacidade(10)
                .preco(new BigDecimal("35.00"))
                .build());

        for (int i = 1; i <= 10; i++) {
            assentoRepository.save(Assento.builder()
                    .evento(evento)
                    .codigo("A" + i)
                    .status(AssentoStatus.LIVRE)
                    .build());
        }

        System.out.println("=== Seed de dados criado com sucesso ===");
        System.out.println("Organizador: organizador@verzel.com / 123456");
        System.out.println("Cliente 1:   cliente1@verzel.com / 123456");
        System.out.println("Cliente 2:   cliente2@verzel.com / 123456");
        System.out.println("Portaria:    portaria@verzel.com / 123456");
    }
}