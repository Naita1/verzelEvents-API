package verzelEvents.repository;

import verzelEvents.entity.Reserva;
import verzelEvents.entity.ReservaStatus;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservaRepository extends JpaRepository<Reserva, UUID> {
    List<Reserva> findByClienteId(UUID clienteId);
    List<Reserva> findByEventoId(UUID eventoId);
    Optional<Reserva> findByIdempotencyKey(String idempotencyKey);
    List<Reserva> findAllByStatusAndExpiresAtBefore(ReservaStatus status, LocalDateTime now);
}