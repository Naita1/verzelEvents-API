package verzelEvents.repository;

import verzelEvents.entity.Ingresso;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IngressoRepository extends JpaRepository<Ingresso, UUID> {
    Optional<Ingresso> findByShareToken(String shareToken);

    Optional<Ingresso> findByReservaId(UUID reservaId);

    List<Ingresso> findByReserva_Cliente_IdOrderByReserva_Evento_DataHoraDesc(UUID clienteId);

    Optional<Ingresso> findByIdAndReserva_Cliente_Id(UUID id, UUID clienteId);

    @Query("""
        select i
        from Ingresso i
        join fetch i.reserva r
        join fetch r.evento e
        where r.cliente.id = :clienteId
        order by e.dataHora desc
        """)
    List<Ingresso> findAllByClienteIdWithDetails(@Param("clienteId") UUID clienteId);
}