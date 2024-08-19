package euclid.lyc_spring.domain.chat.commission;

import euclid.lyc_spring.domain.enums.Color;
import euclid.lyc_spring.domain.enums.Fit;
import euclid.lyc_spring.domain.enums.Material;
import euclid.lyc_spring.domain.enums.Style;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class CommissionStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column
    private Style style;

    @Enumerated(EnumType.STRING)
    @Column
    private Fit fit;

    @Enumerated(EnumType.STRING)
    @Column
    private Material material;

    @Enumerated(EnumType.STRING)
    @Column
    private Color color;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_id", nullable = false)
    private Commission commission;
}
