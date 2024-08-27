package euclid.lyc_spring.domain.chat.commission.commission_info;

import euclid.lyc_spring.domain.enums.Material;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class CommissionInfoMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Material material;

    @Column(nullable = false)
    private Boolean isPrefer;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_info_id", nullable = false)
    private CommissionInfo commissionInfo;

    protected CommissionInfoMaterial() {}

    @Builder
    public CommissionInfoMaterial(Material material, Boolean isPrefer, CommissionInfo commissionInfo) {
        this.material = material;
        this.isPrefer = isPrefer;
        this.commissionInfo = commissionInfo;
    }
}
