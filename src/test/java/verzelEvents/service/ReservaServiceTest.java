package verzelEvents.service;

import verzelEvents.entity.*;
import verzelEvents.repository.AssentoRepository;
import verzelEvents.repository.EventoRepository;
import verzelEvents.repository.ReservaRepository;
import verzelEvents.repository.UsuarioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock private ReservaRepository reservaRepository;
    @Mock private AssentoRepository assentoRepository;
    @Mock private EventoRepository eventoRepository;
    @Mock private UsuarioRepository usuarioRepository;

    @InjectMocks private ReservaService reservaService;

    @Test
    @DisplayName("Deve expirar reservas vencidas e liberar o assento")
    void deveExpirarReservasVencidas() {
        Assento assento = Assento.builder()
                .status(AssentoStatus.RESERVADO)
                .build();

        Reserva reserva = Reserva.builder()
                .status(ReservaStatus.PENDENTE)
                .expiresAt(LocalDateTime.now().minusMinutes(1))
                .assento(assento)
                .build();

        when(reservaRepository.findAllByStatusAndExpiresAtBefore(eq(ReservaStatus.PENDENTE), any(LocalDateTime.class)))
                .thenReturn(List.of(reserva));

        reservaService.releaseExpiredReservations();
        assertEquals(ReservaStatus.EXPIRADA, reserva.getStatus());
        assertEquals(AssentoStatus.LIVRE, assento.getStatus());
        verify(reservaRepository, times(1)).save(reserva);
        verify(assentoRepository, times(1)).save(assento);
    }
}