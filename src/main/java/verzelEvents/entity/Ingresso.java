package verzelEvents.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "ingressos", indexes = {
        @Index(name = "idx_ingresso_share_token", columnList = "shareToken", unique = true),
        @Index(name = "idx_ingresso_reserva_id", columnList = "reserva_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingresso {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "reserva_id", nullable = false, unique = true)
    private Reserva reserva;

    @Column(name = "qr_hash", nullable = false)
    private String qrHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private IngressoStatus status;

    @Column(name = "share_token", unique = true)
    private String shareToken;
}