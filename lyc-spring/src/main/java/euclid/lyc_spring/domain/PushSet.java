package euclid.lyc_spring.domain;

import euclid.lyc_spring.dto.request.MemberRequestDTO;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class PushSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean dm;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean feed;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean schedule;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean likeMark;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean event;

    @Column(columnDefinition = "BIT DEFAULT 1")
    private Boolean ad;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    public void updatePushSet(MemberRequestDTO.PushSetDTO pushSetDTO) {
        this.dm = pushSetDTO.getDm();
        this.feed = pushSetDTO.getFeed();
        this.schedule = pushSetDTO.getSchedule();
        this.likeMark = pushSetDTO.getLikeMark();
        this.event = pushSetDTO.getEvent();
        this.ad = pushSetDTO.getAd();
    }
}