package verzelEvents.repository;

import verzelEvents.entity.Assento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface AssentoRepository extends JpaRepository<Assento, UUID> {
    List<Assento> findByEventoId(UUID eventoId);
}