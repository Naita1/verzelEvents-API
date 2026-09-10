package verzelEvents.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "ingressos", indexes = {
        @Index(name = "idx_ingresso_share_token", columnList = "shareToken", unique = true),
        @Index(name = "idx_ingresso_reserva_id", columnList = "reserva_id")
})
@Getter
@Setter
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

    @Column(name = "qr_hash", nullable = false, length = 100)
    private String qrHash;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private IngressoStatus status;

    @Column(name = "share_token", unique = true, length = 100)
    private String shareToken;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingresso ingresso = (Ingresso) o;
        return id != null && Objects.equals(id, ingresso.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}