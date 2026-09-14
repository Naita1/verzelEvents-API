package verzelEvents.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import verzelEvents.entity.Assento;
import verzelEvents.entity.Reserva;
import verzelEvents.entity.Usuario;
import verzelEvents.dto.request.CreateReservaRequest;
import verzelEvents.exception.InvalidOperationException;
import verzelEvents.exception.ResourceNotFoundException;
import verzelEvents.repository.AssentoRepository;
import verzelEvents.repository.ReservaRepository;
import verzelEvents.repository.UsuarioRepository;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    private ReservaRepository reservaRepository;

    @Mock
    private AssentoRepository assentoRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ReservaService reservaService;

    @Test
    @DisplayName("Deve criar uma nova reserva com sucesso quando o assento estiver LIVRE")
    void deveCriarReservaComSucesso() {
        // Arrange
        UUID assentoId = UUID.randomUUID();
        String usuarioEmail = "cliente@verzel.com";
        String chaveIdempotencia = UUID.randomUUID().toString();

        CreateReservaRequest request = mock(CreateReservaRequest.class);
        lenient().when(request.getAssentoId()).thenReturn(assentoId);

        Assento assentoMock = mock(Assento.class);
        Usuario usuarioMock = mock(Usuario.class);
        Reserva reservaMock = mock(Reserva.class);

        when(reservaRepository.findByIdempotencyKey(chaveIdempotencia)).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(usuarioEmail)).thenReturn(Optional.of(usuarioMock));
        when(assentoRepository.findById(assentoId)).thenReturn(Optional.of(assentoMock));
        
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reservaMock);

        // Act
        Object resultado = reservaService.createReserva(request, usuarioEmail);

        // Assert
        assertThat(resultado).isNotNull();
        verify(reservaRepository, times(1)).save(any(Reserva.class));
        verify(assentoRepository, times(1)).save(any(Assento.class));
    }

    @Test
    @DisplayName("Deve retornar a reserva existente se a chave de idempotência já constar no banco")
    void deveRetornarReservaExistenteQuandoChaveIdempotenciaFornecida() {
        // Arrange
        UUID assentoId = UUID.randomUUID();
        String usuarioEmail = "cliente@verzel.com";
        String chaveIdempotencia = "chave-reenvio-123";

        CreateReservaRequest request = mock(CreateReservaRequest.class);
        lenient().when(request.getAssentoId()).thenReturn(assentoId);

        Reserva reservaExistenteMock = mock(Reserva.class);

        when(reservaRepository.findByIdempotencyKey(chaveIdempotencia)).thenReturn(Optional.of(reservaExistenteMock));

        // Act
        Object resultado = reservaService.createReserva(request, usuarioEmail);

        // Assert
        assertThat(resultado).isNotNull();
        verify(usuarioRepository, never()).findByEmail(any());
        verify(assentoRepository, never()).findById(any());
        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar InvalidOperationException quando o assento já estiver RESERVADO ou VENDIDO")
    void deveLancarExcecaoQuandoAssentoNaoEstiverLivre() {
        // Arrange
        UUID assentoId = UUID.randomUUID();
        String usuarioEmail = "cliente@verzel.com";

        CreateReservaRequest request = mock(CreateReservaRequest.class);
        lenient().when(request.getAssentoId()).thenReturn(assentoId);

        Usuario usuarioMock = mock(Usuario.class);
        Assento assentoMock = mock(Assento.class);

        lenient().when(reservaRepository.findByIdempotencyKey(any())).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(usuarioEmail)).thenReturn(Optional.of(usuarioMock));
        when(assentoRepository.findById(assentoId)).thenReturn(Optional.of(assentoMock));

        // Act & Assert
        assertThrows(InvalidOperationException.class, () -> 
            reservaService.createReserva(request, usuarioEmail)
        );

        verify(reservaRepository, never()).save(any(Reserva.class));
    }

    @Test
    @DisplayName("Deve lançar ResourceNotFoundException quando o assento não for encontrado")
    void deveLancarExcecaoQuandoAssentoNaoEncontrado() {
        // Arrange
        UUID assentoId = UUID.randomUUID();
        String usuarioEmail = "cliente@verzel.com";

        CreateReservaRequest request = mock(CreateReservaRequest.class);
        lenient().when(request.getAssentoId()).thenReturn(assentoId);

        Usuario usuarioMock = mock(Usuario.class);

        lenient().when(reservaRepository.findByIdempotencyKey(any())).thenReturn(Optional.empty());
        when(usuarioRepository.findByEmail(usuarioEmail)).thenReturn(Optional.of(usuarioMock));
        when(assentoRepository.findById(assentoId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> 
            reservaService.createReserva(request, usuarioEmail)
        );

        verify(reservaRepository, never()).save(any(Reserva.class));
    }
}