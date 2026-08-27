package verzelEvents.config;

import verzelEvents.entity.*;
import verzelEvents.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;
    private final AssentoRepository assentoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("Banco de dados já populado. Seed não será executado.");
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
                .tipo("CINEMA")
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

        log.info("=== Seed de dados criado com sucesso ===");
        log.info("Organizador: organizador@verzel.com / 123456");
        log.info("Cliente 1:   cliente1@verzel.com / 123456");
        log.info("Cliente 2:   cliente2@verzel.com / 123456");
        log.info("Portaria:    portaria@verzel.com / 123456");
    }
}