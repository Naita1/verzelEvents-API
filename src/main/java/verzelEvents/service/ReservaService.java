package verzelEvents.service;

import verzelEvents.dto.request.CreateReservaRequest;
import verzelEvents.dto.response.ReservaResponse;
import verzelEvents.entity.*;
import verzelEvents.exception.SeatAlreadyReservedException;
import verzelEvents.repository.*;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private static final int RESERVA_TTL_MINUTOS = 5;

    private final ReservaRepository reservaRepository;
    private final AssentoRepository assentoRepository;
    private final EventoRepository eventoRepository;
    private final UsuarioRepository usuarioRepository;

    @Transactional
    public ReservaResponse createReserva(CreateReservaRequest request, String clienteEmail) {
        Usuario cliente = usuarioRepository.findByEmail(clienteEmail)
                .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado"));

        Evento evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new IllegalArgumentException("Evento não encontrado"));

        Assento assento = assentoRepository.findById(request.getAssentoId())
                .orElseThrow(() -> new IllegalArgumentException("Assento não encontrado"));

        if (assento.getStatus() != AssentoStatus.LIVRE) {
            throw new SeatAlreadyReservedException("Este assento já está reservado ou vendido");
        }

        try {
            assento.setStatus(AssentoStatus.RESERVADO);
            assentoRepository.saveAndFlush(assento);
        } catch (OptimisticLockException | ObjectOptimisticLockingFailureException e) {
            throw new SeatAlreadyReservedException("Este assento acabou de ser reservado por outro cliente");
        }

        Reserva reserva = Reserva.builder()
                .evento(evento)
                .cliente(cliente)
                .assento(assento)
                .status(ReservaStatus.PENDENTE)
                .expiresAt(LocalDateTime.now().plusMinutes(RESERVA_TTL_MINUTOS))
                .idempotencyKey(request.getIdempotencyKey())
                .build();

        reservaRepository.save(reserva);

        return toResponse(reserva);
    }

    @Transactional
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        reservaRepository.findAll().stream()
                .filter(r -> r.getStatus() == ReservaStatus.PENDENTE && r.getExpiresAt().isBefore(now))
                .forEach(r -> {
                    r.setStatus(ReservaStatus.EXPIRADA);
                    r.getAssento().setStatus(AssentoStatus.LIVRE);
                    reservaRepository.save(r);
                    assentoRepository.save(r.getAssento());
                });
    }

    private ReservaResponse toResponse(Reserva reserva) {
        return new ReservaResponse(
                reserva.getId(),
                reserva.getStatus().name(),
                reserva.getExpiresAt(),
                reserva.getEvento().getTitulo(),
                reserva.getAssento() != null ? reserva.getAssento().getCodigo() : null,
                reserva.getCliente().getNome()
        );
    }
}