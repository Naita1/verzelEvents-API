package verzelEvents.service;

import org.springframework.scheduling.annotation.Scheduled;
import verzelEvents.dto.request.CreateReservaRequest;
import verzelEvents.dto.response.ReservaResponse;
import verzelEvents.exception.ResourceNotFoundException;
import verzelEvents.entity.*;
import verzelEvents.exception.SeatAlreadyReservedException;
import verzelEvents.repository.*;
import jakarta.persistence.OptimisticLockException;
import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

import java.time.LocalDateTime;

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

        if (request.getIdempotencyKey() != null && !request.getIdempotencyKey().isBlank()) {
            var reservaExistente = reservaRepository.findByIdempotencyKey(request.getIdempotencyKey());
            if (reservaExistente.isPresent()) {
                return toResponse(reservaExistente.get());
            }
        }

        Usuario cliente = usuarioRepository.findByEmail(clienteEmail)
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado"));

        Evento evento = eventoRepository.findById(request.getEventoId())
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado: " + request.getEventoId()));

        Assento assento = assentoRepository.findById(request.getAssentoId())
                .orElseThrow(() -> new ResourceNotFoundException("Assento não encontrado: " + request.getAssentoId()));

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

    @Scheduled(fixedRate = 60000)
    @Transactional
    public void releaseExpiredReservations() {
        LocalDateTime now = LocalDateTime.now();
        List<Reserva> expiredReservations = reservaRepository.findAllByStatusAndExpiresAtBefore(ReservaStatus.PENDENTE, now);

        if (!expiredReservations.isEmpty()) {
            for (Reserva r : expiredReservations) {
                r.setStatus(ReservaStatus.EXPIRADA);
                if (r.getAssento() != null) {
                    r.getAssento().setStatus(AssentoStatus.LIVRE);
                }
            }
            reservaRepository.saveAll(expiredReservations);
        }
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