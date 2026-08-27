package verzelEvents.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "validacoes", indexes = {
        @Index(name = "idx_validacao_ingresso_id", columnList = "ingresso_id")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Validacao {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "ingresso_id")
    private Ingresso ingresso;

    @ManyToOne
    @JoinColumn(name = "portaria_id", nullable = false)
    private Usuario portaria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultadoValidacao resultado;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;
}