package verzelEvents.config;

import verzelEvents.entity.*;
import verzelEvents.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Profile("!prod")
public class DataSeeder implements CommandLineRunner {

    private static final Logger log = LoggerFactory.getLogger(DataSeeder.class);

    private final UsuarioRepository usuarioRepository;
    private final EventoRepository eventoRepository;
    private final AssentoRepository assentoRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) {
        if (usuarioRepository.count() > 0) {
            log.info("Banco de dados já populado. Seed não será executado.");
            return; 
        }

        String defaultPassword = passwordEncoder.encode("123456");

        Usuario organizador = Usuario.builder()
                .nome("Organizador Demo")
                .email("organizador@verzel.com")
                .senha(defaultPassword)
                .role(RoleEnum.ORGANIZADOR)
                .ativo(true)
                .build();

        Usuario cliente1 = Usuario.builder()
                .nome("Cliente Um")
                .email("cliente1@verzel.com")
                .senha(defaultPassword)
                .role(RoleEnum.CLIENTE)
                .ativo(true)
                .build();

        Usuario cliente2 = Usuario.builder()
                .nome("Cliente Dois")
                .email("cliente2@verzel.com")
                .senha(defaultPassword)
                .role(RoleEnum.CLIENTE)
                .ativo(true)
                .build();

        Usuario portaria = Usuario.builder()
                .nome("Portaria Demo")
                .email("portaria@verzel.com")
                .senha(defaultPassword)
                .role(RoleEnum.PORTARIA)
                .ativo(true)
                .build();

        usuarioRepository.saveAll(List.of(organizador, cliente1, cliente2, portaria));

        Evento evento = eventoRepository.save(Evento.builder()
                .organizador(organizador)
                .titulo("Matrix Resurrections")
                .tipo("CINEMA")
                .dataHora(LocalDateTime.now().plusDays(7))
                .local("Sala 1 - Cine Verzel")
                .capacidade(10)
                .preco(new BigDecimal("35.00"))
                .build());

        List<Assento> assentos = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            assentos.add(Assento.builder()
                    .evento(evento)
                    .codigo("A" + i)
                    .status(AssentoStatus.LIVRE)
                    .build());
        }
        assentoRepository.saveAll(assentos);

        log.info("=== Seed de dados criado com sucesso ===");
        log.info("Organizador: organizador@verzel.com / 123456");
        log.info("Cliente 1:   cliente1@verzel.com / 123456");
        log.info("Cliente 2:   cliente2@verzel.com / 123456");
        log.info("Portaria:    portaria@verzel.com / 123456");
    }
}