package euclid.lyc_spring.domain.chat.commission.commission_info;

import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.enums.BottomSize;
import euclid.lyc_spring.domain.enums.TopSize;
import euclid.lyc_spring.dto.request.InfoRequestDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DynamicUpdate
@DynamicInsert
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
    private List<CommissionInfoBodyType> commissionInfoBodyTypeList;

    @OneToMany(mappedBy = "commissionInfo", cascade = CascadeType.ALL)
    private List<CommissionInfoFit> commissionInfoFitList;

    @OneToMany(mappedBy = "commissionInfo", cascade = CascadeType.ALL)
    private List<CommissionInfoMaterial> commissionInfoMaterialList;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_id", nullable = false)
    private Commission commission;

    protected CommissionInfo() {}

    @Builder
    public CommissionInfo(Short height, Short weight, TopSize topSize,
                          BottomSize bottomSize, String text) {
        this.height = height;
        this.weight = weight;
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.text = text;
        commissionInfoStyleList = new ArrayList<>();
        commissionInfoBodyTypeList = new ArrayList<>();
        commissionInfoFitList = new ArrayList<>();
        commissionInfoMaterialList = new ArrayList<>();
    }

    //=== Methods ===//

    public void addCommissionInfoStyle(CommissionInfoStyle infoStyle) {
        commissionInfoStyleList.add(infoStyle);
        infoStyle.setCommissionInfo(this);
    }

    public void addCommissionInfoFit(CommissionInfoFit infoFit) {
        commissionInfoFitList.add(infoFit);
        infoFit.setCommissionInfo(this);
    }

    public void addCommissionInfoMaterial(CommissionInfoMaterial infoMaterial) {
        commissionInfoMaterialList.add(infoMaterial);
        infoMaterial.setCommissionInfo(this);
    }

    public void addCommissionInfoBodyType(CommissionInfoBodyType infoBodyType) {
        commissionInfoBodyTypeList.add(infoBodyType);
        infoBodyType.setCommissionInfo(this);
    }

    public void clear(){
        commissionInfoStyleList = new ArrayList<>();
        commissionInfoBodyTypeList = new ArrayList<>();
        commissionInfoFitList = new ArrayList<>();
        commissionInfoMaterialList = new ArrayList<>();
    }

    public void reloadInfo(InfoRequestDTO.BasicInfoDTO basicInfoDTO){
        this.height = basicInfoDTO.getHeight();
        this.weight = basicInfoDTO.getWeight();
        this.topSize = basicInfoDTO.getTopSize();
        this.bottomSize = basicInfoDTO.getBottomSize();
        this.text = basicInfoDTO.getText();
    }
}

