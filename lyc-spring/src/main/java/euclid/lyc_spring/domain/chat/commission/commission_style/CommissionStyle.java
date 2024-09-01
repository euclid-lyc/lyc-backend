package euclid.lyc_spring.domain.chat.commission.commission_style;

import euclid.lyc_spring.domain.chat.commission.Commission;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

// CommissionStyle 관련 도메인이 추가되어 새롭게 패키지 만들었음
@Getter
@Entity
public class CommissionStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    // 원하는 style에 여러가지 선택할 수 있기때문에 List로 변경 후 관련 도메인 추가함
    @OneToMany(mappedBy = "commissionStyle", cascade = CascadeType.ALL)
    @Column
    private List<CommissionStyleStyle> style;

    // 원하는 fit에 여러가지 선택할 수 있기때문에 List로 변경 후 관련 도메인 추가함
    @OneToMany(mappedBy = "commissionStyle", cascade = CascadeType.ALL)
    @Column
    private List<CommissionStyleFit> fit;

    // 원하는 materiale에 여러가지 선택할 수 있기때문에 List로 변경 후 관련 도메인 추가함
    @OneToMany(mappedBy = "commissionStyle", cascade = CascadeType.ALL)
    @Column
    private List<CommissionStyleMaterial> material;

    // 원하는 color에 여러가지 선택할 수 있기때문에 List로 변경 후 관련 도메인 추가함
    @OneToMany(mappedBy = "commissionStyle", cascade = CascadeType.ALL)
    @Column
    private List<CommissionStyleColor> color;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_id", nullable = false)
    private Commission commission;

    @Builder
    public CommissionStyle() {
        this.style = new ArrayList<>();
        this.fit = new ArrayList<>();
        this.material = new ArrayList<>();
        this.color = new ArrayList<>();
    }

    //=== method ===//

    public void addCommissionStyleStyle(CommissionStyleStyle commissionStyleStyle) {
        style.add(commissionStyleStyle);
        commissionStyleStyle.setCommissionStyle(this);
    }

    public void addCommissionStyleFit(CommissionStyleFit commissionStyleFit) {
        fit.add(commissionStyleFit);
        commissionStyleFit.setCommissionStyle(this);
    }

    public void addCommissionStyleMaterial(CommissionStyleMaterial commissionStyleMaterial) {
        material.add(commissionStyleMaterial);
        commissionStyleMaterial.setCommissionStyle(this);
    }

    public void addCommissionStyleColor(CommissionStyleColor commissionStyleColor) {
        color.add(commissionStyleColor);
        commissionStyleColor.setCommissionStyle(this);
    }

    public void clear(){
        style = new ArrayList<>();
        material = new ArrayList<>();
        color = new ArrayList<>();
        fit = new ArrayList<>();
    }
}
