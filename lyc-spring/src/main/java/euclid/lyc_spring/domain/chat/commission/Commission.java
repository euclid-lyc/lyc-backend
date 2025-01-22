package euclid.lyc_spring.domain.chat.commission;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionBodyType;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionFit;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionMaterial;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionStyle;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionHopeColor;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionHopeFit;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionHopeMaterial;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionHopeStyle;
import euclid.lyc_spring.domain.enums.BottomSize;
import euclid.lyc_spring.domain.enums.CommissionStatus;
import euclid.lyc_spring.domain.enums.TopSize;
import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.dto.request.InfoDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

/*-------------------------------------------------- 의뢰서 정보 --------------------------------------------------*/

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommissionStatus status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @Setter
    @Column
    private LocalDateTime finishedAt;

    @Setter
    @OneToOne(mappedBy = "commission", cascade = CascadeType.ALL)
    private Chat chat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member director;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT)) // nullable
    private Posting review;

/*-------------------------------------------------- 1페이지 (기본정보) --------------------------------------------------*/

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

    @Builder.Default
    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionStyle> styles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionBodyType> bodyTypes = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionFit> fits = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionMaterial> materials = new ArrayList<>();

/*-------------------------------------------------- 2페이지 (희망스타일) --------------------------------------------------*/

    @Builder.Default
    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionHopeStyle> hopeStyles = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionHopeFit> hopeFits = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionHopeMaterial> hopeMaterials = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionHopeColor> hopeColors = new ArrayList<>();


/*-------------------------------------------------- 3페이지 (기타사항) --------------------------------------------------*/

    @Column
    private LocalDate dateToUse;

    @Column
    private LocalDate desiredDate;

    @Column(nullable = false)
    private Integer minPrice;

    @Column(nullable = false)
    private Integer maxPrice;

    @Column(columnDefinition = "text")
    private String freeText;

    @Column
    private Boolean isShared;

/*-------------------------------------------------- 연관관계 메서드 --------------------------------------------------*/

    public void addBodyType(CommissionBodyType bodyType) {
        bodyTypes.add(bodyType);
    }

    public void addStyle(CommissionStyle style) {
        styles.add(style);
    }

    public void addFit(CommissionFit fit) {
        fits.add(fit);
    }

    public void addMaterial(CommissionMaterial material) {
        materials.add(material);
    }

    public void addHopeColor(CommissionHopeColor color) {
        hopeColors.add(color);
    }

    public void addHopeStyle(CommissionHopeStyle style) {
        hopeStyles.add(style);
    }

    public void addHopeFit(CommissionHopeFit fit) {
        hopeFits.add(fit);
    }

    public void addHopeMaterial(CommissionHopeMaterial material) {
        hopeMaterials.add(material);
    }

    public void updateCommissionOther(InfoDTO.OtherMattersDTO otherMattersDTO) {
        this.dateToUse = otherMattersDTO.getDateToUse();
        this.desiredDate = otherMattersDTO.getDesiredDate();
        this.minPrice = otherMattersDTO.getMinPrice();
        this.maxPrice = otherMattersDTO.getMaxPrice();
        this.freeText = otherMattersDTO.getText();
        this.isShared = otherMattersDTO.getIsShared();
    }

    public void updateCommissionInfo(InfoDTO.BasicInfoDTO basicInfoDTO) {
        this.height = basicInfoDTO.getHeight();
        this.weight = basicInfoDTO.getWeight();
        this.topSize = basicInfoDTO.getTopSize();
        this.bottomSize = basicInfoDTO.getBottomSize();
        this.text = basicInfoDTO.getText();
    }

    public void deleteAllBodyTypes() {
        bodyTypes = new ArrayList<>();
    }

    public void deleteAllStyles() {
        styles = new ArrayList<>();
    }

    public void deleteAllFits() {
        fits = new ArrayList<>();
    }

    public void deleteAllMaterials() {
        materials = new ArrayList<>();
    }

    public void deleteAllHopeColors() {
        hopeColors = new ArrayList<>();
    }

    public void deleteAllHopeFits() {
        hopeFits = new ArrayList<>();
    }

    public void deleteAllHopeMaterials() {
        hopeMaterials = new ArrayList<>();
    }

    public void deleteAllHopeStyles() {
        hopeStyles = new ArrayList<>();
    }

}