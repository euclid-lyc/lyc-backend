package euclid.lyc_spring.domain.info;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.BottomSize;
import euclid.lyc_spring.domain.enums.TopSize;
import euclid.lyc_spring.dto.request.InfoRequestDTO;
import euclid.lyc_spring.dto.request.MemberRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Builder.Default
    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
    private List<InfoStyle> infoStyleList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
    private List<InfoMaterial> infoMaterialList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
    private List<InfoFit> infoFitList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
    private List<InfoBodyType> infoBodyTypeList = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;


/*-------------------------------------------------- 연관관계 메서드 --------------------------------------------------*/

    public void addInfoStyle(InfoStyle infoStyle) {
        infoStyleList.add(infoStyle);
    }

    public void addInfoMaterial(InfoMaterial infoMaterial) {
        infoMaterialList.add(infoMaterial);
    }

    public void addInfoFit(InfoFit infoFit) {
        infoFitList.add(infoFit);
    }

    public void addInfoBodyType(InfoBodyType infoBodyType) {
        infoBodyTypeList.add(infoBodyType);
    }

    public void updateAddress(MemberRequestDTO.AddressReqDTO addressReqDTO) {
        this.postalCode = addressReqDTO.getPostalCode();
        this.address = addressReqDTO.getAddress();
        this.detailAddress = addressReqDTO.getDetailAddress();
    }

    public void updateInfo(InfoRequestDTO.StyleInfoDTO styleInfoDTO) {
        this.isPublic = styleInfoDTO.getIsPublic();
        this.topSize = styleInfoDTO.getTopSize();
        this.bottomSize = styleInfoDTO.getBottomSize();
        this.height = styleInfoDTO.getHeight();
        this.weight = styleInfoDTO.getWeight();
        this.text = styleInfoDTO.getDetails();
    }

    //=== deleteMethods ===//
    public void deleteInfoStyle(InfoStyle infoStyle) {
        infoStyleList.remove(infoStyle);
    }

    public void deleteInfoFit(InfoFit infoFit) {
        infoFitList.remove(infoFit);
    }

    public void deleteInfoMaterial(InfoMaterial infoMaterial) {
        infoMaterialList.remove(infoMaterial);
    }

    public void deleteInfoBodyType(InfoBodyType infoBodyType) {
        infoBodyTypeList.remove(infoBodyType);
    }
}