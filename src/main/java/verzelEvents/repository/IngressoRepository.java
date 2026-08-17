package verzelEvents.repository;

import verzelEvents.entity.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngressoRepository extends JpaRepository<Ingresso, UUID> {
    Optional<Ingresso> findByShareToken(String shareToken);
    Optional<Ingresso> findByReservaId(UUID reservaId);
    List<Ingresso> findByReserva_Cliente_Id(UUID clienteId);
}