package euclid.lyc_spring.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Report {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean abuse; // 욕설 및 비방

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean obsceneContent; // 선정적인 콘텐츠

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean privacy; // 사생활 침해 및 개인정보 유출

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean spam; // 스팸 및 부적절한 광고

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean infringement; // 저작권 및 지적재산권 침해

    @Column
    private String description; // 기타 사유 작성

    @Column(nullable = false, columnDefinition = "BIT DEFAULT 0")
    private Boolean isProcessed; // 신고 처리 여부

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Builder
    public Report(Boolean abuse, Boolean obsceneContent, Boolean privacy,
                  Boolean spam, Boolean infringement, String description,
                  Member member) {
        this.abuse = abuse;
        this.obsceneContent = obsceneContent;
        this.privacy = privacy;
        this.spam = spam;
        this.infringement = infringement;
        this.description = description;
        this.isProcessed = false;
        this.member = member;
    }
}
