package verzelEvents.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import verzelEvents.dto.request.CreateReservaRequest;
import verzelEvents.entity.Assento;
import verzelEvents.entity.AssentoStatus;
import verzelEvents.entity.Evento;
import verzelEvents.entity.RoleEnum;
import verzelEvents.entity.Usuario;
import verzelEvents.exception.SeatAlreadyReservedException;
import verzelEvents.repository.AssentoRepository;
import verzelEvents.repository.EventoRepository;
import verzelEvents.repository.ReservaRepository;
import verzelEvents.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.flyway.enabled=true",
        "spring.flyway.locations=classpath:/",
        "app.jwt.secret=integration-test-secret-key",
        "app.tmdb.api-key=integration-test-key",
        "app.qr.secret=integration-test-qr-secret"
})
@ActiveProfiles("prod")
@Testcontainers
class ReservaConcurrencyTest {

    @SuppressWarnings("resource")
    @Container
    static final PostgreSQLContainer<?> POSTGRES = new PostgreSQLContainer<>("postgres:16-alpine")
            .withDatabaseName("verzel_events_test")
            .withUsername("postgres")
            .withPassword("postgres");

    @Autowired
    private ReservaService reservaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private AssentoRepository assentoRepository;

    @Autowired
    private ReservaRepository reservaRepository;

    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES::getUsername);
        registry.add("spring.datasource.password", POSTGRES::getPassword);
    }

    @BeforeEach
    void cleanDatabase() {
        reservaRepository.deleteAll();
        assentoRepository.deleteAll();
        eventoRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    @Test
    void devePermitirApenasUmaReservaParaMesmoAssento() throws Exception {
        Usuario cliente = usuarioRepository.save(Usuario.builder()
                .nome("Cliente Concorrente")
                .email("cliente-concorrente@verzel.com")
                .senha("senha")
                .role(RoleEnum.CLIENTE)
                .ativo(true)
                .build());

        Evento evento = eventoRepository.save(Evento.builder()
                .organizador(cliente)
                .titulo("Evento Concorrente")
                .tipo("SHOW")
                .dataHora(LocalDateTime.now().plusDays(1))
                .local("Sala 1")
                .capacidade(1)
                .preco(new BigDecimal("10.00"))
                .build());

        Assento assento = assentoRepository.save(Assento.builder()
                .evento(evento)
                .codigo("A1")
                .status(AssentoStatus.LIVRE)
                .build());

        int attempts = 10;
        CountDownLatch ready = new CountDownLatch(attempts);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(attempts);
        List<Future<?>> futures = new ArrayList<>();

        try {
            for (int index = 0; index < attempts; index++) {
                futures.add(executor.submit(() -> {
                    ready.countDown();
                    await(start);
                    CreateReservaRequest request = new CreateReservaRequest();
                    request.setEventoId(evento.getId());
                    request.setAssentoId(assento.getId());
                    return reservaService.createReserva(request, cliente.getEmail());
                }));
            }

            assertThat(ready.await(10, TimeUnit.SECONDS)).isTrue();
            start.countDown();

            int successes = 0;
            List<Throwable> failures = new ArrayList<>();
            for (Future<?> future : futures) {
                try {
                    future.get(30, TimeUnit.SECONDS);
                    successes++;
                } catch (ExecutionException exception) {
                    failures.add(exception.getCause());
                }
            }

            assertThat(successes).isEqualTo(1);
            assertThat(failures).hasSize(attempts - 1);
            assertThat(failures).allMatch(this::isConcurrencyFailure);
            assertThat(reservaRepository.count()).isEqualTo(1);
            assertThat(assentoRepository.findById(assento.getId()))
                    .get()
                    .extracting(Assento::getStatus)
                    .isEqualTo(AssentoStatus.RESERVADO);
        } finally {
            executor.shutdownNow();
        }
    }

    private boolean isConcurrencyFailure(Throwable failure) {
        return failure instanceof SeatAlreadyReservedException
                || failure instanceof org.springframework.orm.ObjectOptimisticLockingFailureException
                || failure.getCause() instanceof org.springframework.orm.ObjectOptimisticLockingFailureException;
    }

    private static void await(CountDownLatch latch) {
        try {
            latch.await();
        } catch (InterruptedException exception) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("Thread interrompida antes da concorrência", exception);
        }
    }
}