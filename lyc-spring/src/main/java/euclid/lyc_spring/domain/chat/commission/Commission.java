package euclid.lyc_spring.domain.chat.commission;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionInfo;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionStyle;
import euclid.lyc_spring.domain.enums.CommissionStatus;
import euclid.lyc_spring.domain.posting.Posting;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@DynamicUpdate
@DynamicInsert
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

    @OneToOne(mappedBy = "commission", cascade = CascadeType.ALL)
    private CommissionInfo commissionInfo;

    @OneToOne(mappedBy = "commission", cascade = CascadeType.ALL)
    private CommissionOther commissionOther;

    @OneToOne(mappedBy = "commission", cascade = CascadeType.ALL)
    private CommissionStyle commissionStyle;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "director_id", nullable = false)
    private Member director;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id") // nullable
    private Posting review;

    protected Commission() {}

    @Builder
    public Commission(CommissionStatus status, LocalDateTime createdAt, Member member, Member director){
        this.status = status;
        this.createdAt = createdAt;
        this.finishedAt = null;
        this.member = member;
        this.director = director;
    }

    //=== Add Methods ===//

    public void setChat(Chat chat) {
        this.chat = chat;
        chat.setCommission(this);
    }

    public void setCommissionInfo(CommissionInfo commissionInfo) {
        this.commissionInfo = commissionInfo;
        commissionInfo.setCommission(this);
    }

    public void setCommissionOther(CommissionOther commissionOther) {
        this.commissionOther = commissionOther;
        commissionOther.setCommission(this);
    }

    public void setCommissionStyle(CommissionStyle commissionStyle) {
        this.commissionStyle = commissionStyle;
        commissionStyle.setCommission(this);
    }

    public void reloadStatus(CommissionStatus status){
        this.status = status;
    }

    public void reloadFinishedAt(LocalDateTime finishedAt){
        this.finishedAt = finishedAt;
    }

    public void reloadCreatedAt(LocalDateTime createdAt){
        this.createdAt = createdAt;
    }

}