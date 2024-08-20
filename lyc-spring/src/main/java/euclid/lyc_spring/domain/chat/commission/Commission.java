package euclid.lyc_spring.domain.chat.commission;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.Schedule;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionInfo;
import euclid.lyc_spring.domain.enums.CommissionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Commission {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommissionStatus status;

    @CreatedDate
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime finishedAt;

    @OneToOne(mappedBy = "commission", cascade = CascadeType.ALL)
    private Chat chat;

    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionInfo> commissionInfoList;

    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionOther> commissionOtherList;

    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionStyle> commissionStyleList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id", nullable = false)
    private Member director;

}