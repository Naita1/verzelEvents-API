package verzelEvents.repository;

import verzelEvents.entity.Validacao;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface ValidacaoRepository extends JpaRepository<Validacao, UUID> {
    List<Validacao> findByIngresso_Reserva_Evento_IdOrderByCreatedAtDesc(UUID eventoId);}