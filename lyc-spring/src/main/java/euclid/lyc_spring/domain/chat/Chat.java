package euclid.lyc_spring.domain.chat;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.mapping.MemberChat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DynamicUpdate
@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @Setter
    @Column
    private LocalDateTime inactive;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_id", nullable = false)
    private Commission commission;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Schedule> scheduleList;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<MemberChat> memberChatList;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<CommissionClothes> commissionClothesList;

    @Setter
    @Column
    private Integer savedClothesCount;

    @Setter
    @Column
    private boolean shareClothesList;

    protected Chat(){}

    @Builder
    public Chat(LocalDateTime createdAt, LocalDateTime updatedAt, Commission commission) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commission = commission;
        this.scheduleList = new ArrayList<>();
        this.memberChatList = new ArrayList<>();
        this.commissionClothesList = new ArrayList<>();
        this.savedClothesCount = 0;
        this.shareClothesList = commission.getCommissionOther().getIsShareClothesList();
    }

    public void addSchedule(Schedule schedule){
        scheduleList.add(schedule);
    }

    public void reloadSavedClothesCount(int count){ this.savedClothesCount = count; }

    public void reloadShareClothesList(boolean share) { this.shareClothesList = share; }
  
    public void addMemberChat(MemberChat chat){ memberChatList.add(chat); }

  
}