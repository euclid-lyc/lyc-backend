package euclid.lyc_spring.domain.commission.commission_info;

import euclid.lyc_spring.domain.commission.Commission;
import euclid.lyc_spring.domain.enums.BottomSize;
import euclid.lyc_spring.domain.enums.TopSize;
import euclid.lyc_spring.domain.info.InfoBodyType;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class CommissionInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column
    private Short height;

    @Column
    private Short weight;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private TopSize topSize;

    @Enumerated(EnumType.STRING)
    @Column(length = 8)
    private BottomSize bottomSize;

    @Column(columnDefinition = "text")
    private String text;

    @OneToMany(mappedBy = "commissionInfo", cascade = CascadeType.ALL)
    private List<CommissionInfoStyle> commissionInfoStyleList;

    @OneToMany(mappedBy = "commissionInfo", cascade = CascadeType.ALL)
    private List<CommissionInfoBodyType> infoBodyTypeList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_id", nullable = false)
    private Commission commission;

}
