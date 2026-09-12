package verzelEvents.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import verzelEvents.entity.Assento;

import java.util.List;
import java.util.UUID;

public interface AssentoRepository extends JpaRepository<Assento, UUID> {
    List<Assento> findByEventoIdOrderByCodigoAsc(UUID eventoId);
}