package euclid.lyc_spring.domain.chat.commission.commission_style;

import euclid.lyc_spring.domain.enums.Color;
import euclid.lyc_spring.domain.enums.Fit;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class CommissionStyleColor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Color color;

    @Column(nullable = false)
    private Boolean isPrefer;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_style_id", nullable = false)
    private CommissionStyle commissionStyle;

    protected CommissionStyleColor() {}

    @Builder
    public CommissionStyleColor(Color color, Boolean isPrefer, CommissionStyle commissionStyle) {
        this.color = color;
        this.isPrefer = isPrefer;
        this.commissionStyle = commissionStyle;
    }
}
