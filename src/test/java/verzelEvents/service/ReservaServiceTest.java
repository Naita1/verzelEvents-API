package verzelEvents.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import verzelEvents.dto.request.CreateReservaRequest;
import verzelEvents.dto.response.ReservaResponse;
import verzelEvents.entity.*;
import verzelEvents.exception.ResourceNotFoundException;
import verzelEvents.exception.SeatAlreadyReservedException;
import verzelEvents.repository.AssentoRepository;
import verzelEvents.repository.EventoRepository;
import verzelEvents.repository.ReservaRepository;
import verzelEvents.repository.UsuarioRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private AssentoRepository assentoRepository;

    @Mock
    private EventoRepository eventoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    @DisplayName("Deve criar uma reserva com sucesso quando o assento estiver LIVRE")
    void deveCriarReservaComSucesso() {
        UUID eventoId = UUID.randomUUID();
        UUID assentoId = UUID.randomUUID();
        String usuarioEmail = "cliente@verzel.com";
        String chaveIdempotencia = "key-123";

        CreateReservaRequest request = new CreateReservaRequest();
        request.setEventoId(eventoId);
        request.setAssentoId(assentoId);
        request.setIdempotencyKey(chaveIdempotencia);

        Usuario cliente = Usuario.builder()
                .nome("Cliente Teste")
                .email(usuarioEmail)
                .build();

        Evento evento = Evento.builder()
                .id(eventoId)
                .titulo("Show do Verão")
                .tipo("MUSICA")
                .local("São Paulo")
                .capacidade(100)
                .preco(new BigDecimal("120.00"))
                .dataHora(LocalDateTime.now().plusDays(1))
                .build();

        Assento assento = Assento.builder()
                .id(assentoId)
                .codigo("A1")
                .status(AssentoStatus.LIVRE)
                .evento(evento)
                .build();

        when(reservaRepository.findByIdempotencyKey(chaveIdempotencia)).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(usuarioEmail)).thenReturn(Optional.of(cliente));
        when(eventoRepository.findById(eventoId)).thenReturn(Optional.of(evento));
        when(assentoRepository.findById(assentoId)).thenReturn(Optional.of(assento));
        when(assentoRepository.saveAndFlush(assento)).thenReturn(assento);
        when(reservaRepository.save(any(Reserva.class))).thenAnswer(invocation -> invocation.getArgument(0));

        ReservaResponse response = reservaService.createReserva(request, usuarioEmail);

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("PENDENTE");
        assertThat(response.eventoTitulo()).isEqualTo("Show do Verão");
        assertThat(response.assentoCodigo()).isEqualTo("A1");
        assertThat(response.cliente()).isEqualTo("Cliente Teste");
        assertThat(assento.getStatus()).isEqualTo(AssentoStatus.RESERVADO);

        verify(reservaRepository).findByIdempotencyKey(chaveIdempotencia);
        verify(usuarioRepository).findByEmail(usuarioEmail);
        verify(eventoRepository).findById(eventoId);
        verify(assentoRepository).findById(assentoId);
        verify(assentoRepository).saveAndFlush(assento);
        verify(reservaRepository).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve retornar a reserva existente quando a chave de idempotência já existir")
    void deveRetornarReservaExistenteQuandoChaveIdempotenciaFornecida() {
        UUID assentoId = UUID.randomUUID();
        String usuarioEmail = "cliente@verzel.com";
        String chaveIdempotencia = "reenvio-123";

        CreateReservaRequest request = new CreateReservaRequest();
        request.setEventoId(UUID.randomUUID());
        request.setAssentoId(assentoId);
        request.setIdempotencyKey(chaveIdempotencia);

        Reserva reservaExistente = Reserva.builder()
                .id(UUID.randomUUID())
                .evento(Evento.builder().titulo("Evento Reenviado").build())
                .cliente(Usuario.builder().nome("Cliente Teste").build())
                .assento(Assento.builder().codigo("B3").build())
                .status(ReservaStatus.PENDENTE)
                .expiresAt(LocalDateTime.now().plusMinutes(5))
                .idempotencyKey(chaveIdempotencia)
                .build();

        when(reservaRepository.findByIdempotencyKey(chaveIdempotencia)).thenReturn(Optional.of(reservaExistente));

        ReservaResponse response = reservaService.createReserva(request, usuarioEmail);

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("PENDENTE");
        assertThat(response.eventoTitulo()).isEqualTo("Evento Reenviado");

        verify(usuarioRepository, never()).findByEmail(anyString());
        verify(eventoRepository, never()).findById(any());
        verify(assentoRepository, never()).findById(any());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar SeatAlreadyReservedException quando o assento já estiver RESERVADO ou VENDIDO")
    void deveLancarExcecaoQuandoAssentoNaoEstiverLivre() {
        UUID eventoId = UUID.randomUUID();
        UUID assentoId = UUID.randomUUID();
        String usuarioEmail = "cliente@verzel.com";

        CreateReservaRequest request = new CreateReservaRequest();
        request.setEventoId(eventoId);
        request.setAssentoId(assentoId);
        request.setIdempotencyKey("key-ocupado");

        Usuario cliente = Usuario.builder().nome("Cliente Teste").email(usuarioEmail).build();
        Evento evento = Evento.builder().id(eventoId).titulo("Show do Verão").build();
        Assento assento = Assento.builder()
                .id(assentoId)
                .codigo("C2")
                .status(AssentoStatus.RESERVADO)
                .evento(evento)
                .build();

        when(reservaRepository.findByIdempotencyKey("key-ocupado")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(usuarioEmail)).thenReturn(Optional.of(cliente));
        when(eventoRepository.findById(eventoId)).thenReturn(Optional.of(evento));
        when(assentoRepository.findById(assentoId)).thenReturn(Optional.of(assento));

        assertThrows(SeatAlreadyReservedException.class, () -> reservaService.createReserva(request, usuarioEmail));

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o assento não for encontrado")
    void deveLancarExcecaoQuandoAssentoNaoEncontrado() {
        UUID eventoId = UUID.randomUUID();
        UUID assentoId = UUID.randomUUID();
        String usuarioEmail = "cliente@verzel.com";

        CreateReservaRequest request = new CreateReservaRequest();
        request.setEventoId(eventoId);
        request.setAssentoId(assentoId);
        request.setIdempotencyKey("key-assento-inexistente");

        Usuario cliente = Usuario.builder().nome("Cliente Teste").email(usuarioEmail).build();
        Evento evento = Evento.builder().id(eventoId).titulo("Show do Verão").build();

        when(reservaRepository.findByIdempotencyKey("key-assento-inexistente")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(usuarioEmail)).thenReturn(Optional.of(cliente));
        when(eventoRepository.findById(eventoId)).thenReturn(Optional.of(evento));
        when(assentoRepository.findById(assentoId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservaService.createReserva(request, usuarioEmail));

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o cliente não for encontrado")
    void deveLancarExcecaoQuandoClienteNaoEncontrado() {
        UUID eventoId = UUID.randomUUID();
        UUID assentoId = UUID.randomUUID();
        String usuarioEmail = "cliente@verzel.com";

        CreateReservaRequest request = new CreateReservaRequest();
        request.setEventoId(eventoId);
        request.setAssentoId(assentoId);
        request.setIdempotencyKey("key-cliente");

        Evento evento = Evento.builder().id(eventoId).titulo("Show do Verão").build();
        Assento assento = Assento.builder().id(assentoId).codigo("D1").status(AssentoStatus.LIVRE).evento(evento).build();

        when(reservaRepository.findByIdempotencyKey("key-cliente")).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(usuarioEmail)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> reservaService.createReserva(request, usuarioEmail));

        verify(eventoRepository, never()).findById(any());
        verify(assentoRepository, never()).findById(any());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }
}