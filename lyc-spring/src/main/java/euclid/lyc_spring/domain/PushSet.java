package euclid.lyc_spring.domain;

import euclid.lyc_spring.dto.request.MemberRequestDTO;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicUpdate
@DynamicInsert
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

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    protected PushSet() {}

    @Builder
    public PushSet(Boolean dm, Boolean feed, Boolean schedule,
                   Boolean likeMark, Boolean event, Boolean ad, Member member) {
        this.dm = dm;
        this.feed = feed;
        this.schedule = schedule;
        this.likeMark = likeMark;
        this.event = event;
        this.ad = ad;
        this.member = member;
    }

    //=== reload Methods ===//

    public void reloadPushSet(MemberRequestDTO.PushSetDTO pushSetDTO) {
        this.dm = pushSetDTO.getDm();
        this.feed = pushSetDTO.getFeed();
        this.schedule = pushSetDTO.getSchedule();
        this.likeMark = pushSetDTO.getLikeMark();
        this.event = pushSetDTO.getEvent();
        this.ad = pushSetDTO.getAd();
    }
}