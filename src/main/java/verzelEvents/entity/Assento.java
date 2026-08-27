package verzelEvents.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "assentos", indexes = {
        @Index(name = "idx_assento_evento_id", columnList = "evento_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Assento {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "evento_id", nullable = false)
    private Evento evento;

    @Column(nullable = false)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AssentoStatus status;

    @Version
    private Long version;
}