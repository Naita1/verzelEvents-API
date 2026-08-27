package verzelEvents.repository;

import verzelEvents.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface EventoRepository extends JpaRepository<Evento, UUID> {
    List<Evento> findByOrganizadorId(UUID organizadorId);
}