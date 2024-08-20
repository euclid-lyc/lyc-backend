package euclid.lyc_spring.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
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
}