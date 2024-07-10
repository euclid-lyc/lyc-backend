package euclid.lyc_spring.domain.commission.commission_info;

import euclid.lyc_spring.domain.enums.Fit;
import euclid.lyc_spring.domain.info.Info;
import jakarta.persistence.*;
import lombok.Getter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_info_id", nullable = false)
    private CommissionInfo commissionInfo;
}
