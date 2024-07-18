package euclid.lyc_spring.domain.info;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.BottomSize;
import euclid.lyc_spring.domain.enums.TopSize;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class Info {

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
    }

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

    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
    private List<InfoStyle> infoStyleList;

    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
    private List<InfoMaterial> infoMaterialList;

    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
    private List<InfoFit> infoFitList;

    @OneToMany(mappedBy = "info", cascade = CascadeType.ALL)
    private List<InfoBodyType> infoBodyTypeList;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    public Info() {
        super();
    }
}
