package verzelEvents.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "reservas")
@Data
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
    @Column(nullable = false)
    private ReservaStatus status;

    @Column(name = "expires_at")
    private LocalDateTime expiresAt;

    @Column(name = "idempotency_key")
    private String idempotencyKey;
}