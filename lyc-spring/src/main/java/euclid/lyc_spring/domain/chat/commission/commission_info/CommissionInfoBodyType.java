package euclid.lyc_spring.domain.chat.commission.commission_info;

import euclid.lyc_spring.domain.enums.BodyType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class CommissionInfoBodyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BodyType bodyType;

    @Column(nullable = false)
    private Boolean isGood;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_info_id", nullable = false)
    private CommissionInfo commissionInfo;

    protected CommissionInfoBodyType() {}

    @Builder
    public CommissionInfoBodyType(BodyType bodyType, Boolean isGood, CommissionInfo commissionInfo) {
        this.bodyType = bodyType;
        this.isGood = isGood;
        this.commissionInfo = commissionInfo;
    }
}
