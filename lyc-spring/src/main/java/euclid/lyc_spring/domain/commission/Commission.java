package euclid.lyc_spring.domain.commission;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.commission.commission_info.CommissionInfo;
import euclid.lyc_spring.domain.enums.CommissionStatus;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;

@Getter
@Entity
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
    private Date createdAt;

    @Column
    private Date finishedAt;

    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<Schedule> scheduleList;

    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionInfo> commissionInfoList;

    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionOther> commissionOtherList;

    @OneToMany(mappedBy = "commission", cascade = CascadeType.ALL)
    private List<CommissionStyle> commissionStyleList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id", nullable = false)
    private Member director;

}