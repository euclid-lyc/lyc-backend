package euclid.lyc_spring.domain.chat.commission.commission_info;

import euclid.lyc_spring.domain.enums.Style;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class CommissionInfoStyle {

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
    @JoinColumn(name = "commission_info_id", nullable = false)
    private CommissionInfo commissionInfo;

    protected CommissionInfoStyle() {}

    @Builder
    public CommissionInfoStyle(Style style, Boolean isPrefer, CommissionInfo commissionInfo) {
        this.style = style;
        this.isPrefer = isPrefer;
        this.commissionInfo = commissionInfo;
    }
}
