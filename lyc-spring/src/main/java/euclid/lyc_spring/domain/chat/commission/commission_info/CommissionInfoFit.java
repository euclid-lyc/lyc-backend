package euclid.lyc_spring.domain.chat.commission.commission_info;

import euclid.lyc_spring.domain.enums.Fit;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class CommissionInfoFit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Fit fit;

    @Column(nullable = false)
    private Boolean isPrefer;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_info_id", nullable = false)
    private CommissionInfo commissionInfo;

    protected CommissionInfoFit() {}

    @Builder
    public CommissionInfoFit(Fit fit, Boolean isPrefer, CommissionInfo commissionInfo) {
        this.fit = fit;
        this.isPrefer = isPrefer;
        this.commissionInfo = commissionInfo;
    }
}
