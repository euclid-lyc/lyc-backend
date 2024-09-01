package euclid.lyc_spring.domain.chat.commission.commission_style;

import euclid.lyc_spring.domain.enums.Style;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
public class CommissionStyleStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Style style;

    @Column(nullable = false)
    private Boolean isPrefer;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_style_id", nullable = false)
    private CommissionStyle commissionStyle;

    protected CommissionStyleStyle() {}

    @Builder
    public CommissionStyleStyle(Style style, Boolean isPrefer, CommissionStyle commissionStyle) {
        this.style = style;
        this.isPrefer = isPrefer;
        this.commissionStyle = commissionStyle;
    }
}
