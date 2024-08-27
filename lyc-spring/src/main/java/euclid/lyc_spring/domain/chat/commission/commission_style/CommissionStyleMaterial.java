package euclid.lyc_spring.domain.chat.commission.commission_style;

import euclid.lyc_spring.domain.enums.Material;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class CommissionStyleMaterial {

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
    @JoinColumn(name = "commission_style_id", nullable = false)
    private CommissionStyle commissionStyle;

    protected CommissionStyleMaterial() {}

    @Builder
    public CommissionStyleMaterial(Material material, Boolean isPrefer, CommissionStyle commissionStyle) {
        this.material = material;
        this.isPrefer = isPrefer;
        this.commissionStyle = commissionStyle;
    }
}
