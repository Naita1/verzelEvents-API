package verzelEvents.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "reservas", indexes = {
        @Index(name = "idx_reserva_idempotency_key", columnList = "idempotencyKey", unique = true),
        @Index(name = "idx_reserva_status_expires_at", columnList = "status, expiresAt")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private Usuario cliente;

    @ManyToOne
    @JoinColumn(name = "assento_id")
    private Assento assento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReservaStatus status;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "idempotency_key", length = 100)
    private String idempotencyKey;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reserva reserva = (Reserva) o;
        return id != null && Objects.equals(id, reserva.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}