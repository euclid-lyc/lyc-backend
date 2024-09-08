package euclid.lyc_spring.domain.info;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.BottomSize;
import euclid.lyc_spring.domain.enums.TopSize;
import euclid.lyc_spring.dto.request.MemberRequestDTO;
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
public class Info {

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

    @Column//(nullable = false)
    private Integer postalCode;

    @Column//(length = 30, nullable = false)
    private String address;

    @Column//(length = 30, nullable = false)
    private String detailAddress;

    @Column(columnDefinition = "text")
    private String text;

    @Column(columnDefinition = "BIT DEFAULT 0")
    private Boolean isPublic;

    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
    private List<InfoStyle> infoStyleList;

    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
    private List<InfoMaterial> infoMaterialList;

    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
    private List<InfoFit> infoFitList;

    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
    private List<InfoBodyType> infoBodyTypeList;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected Info() {}

    @Builder
    public Info(Member member, Short height, Short weight, TopSize topSize, BottomSize bottomSize,
                Integer postalCode, String address, String detailAddress, String text) {
        this.member = member;
        this.height = height;
        this.weight = weight;
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.postalCode = postalCode;
        this.address = address;
        this.detailAddress = detailAddress;
        this.text = text;
        this.isPublic = false;
        infoStyleList = new ArrayList<>();
        infoMaterialList = new ArrayList<>();
        infoFitList = new ArrayList<>();
        infoBodyTypeList = new ArrayList<>();
    }


    //=== add Methods ===//

    public void addInfoStyle(InfoStyle infoStyle) {
        infoStyleList.add(infoStyle);
        infoStyle.setInfo(this);
    }

    public void addInfoMaterial(InfoMaterial infoMaterial) {
        infoMaterialList.add(infoMaterial);
        infoMaterial.setInfo(this);
    }

    public void addInfoFit(InfoFit infoFit) {
        infoFitList.add(infoFit);
        infoFit.setInfo(this);
    }

    public void addInfoBodyType(InfoBodyType infoBodyType) {
        infoBodyTypeList.add(infoBodyType);
        infoBodyType.setInfo(this);
    }

    //=== reload Methods ===//
    public void reloadAdrress(MemberRequestDTO.AddressDTO addressDTO) {
        this.postalCode = addressDTO.getPostalCode();
        this.address = addressDTO.getAddress();
        this.detailAddress = addressDTO.getDetailAddress();
    }

}