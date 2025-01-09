package euclid.lyc_spring.domain.chat;

import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.mapping.MemberChat;
import jakarta.persistence.*;
import lombok.*;
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
@Builder
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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
    @Column(columnDefinition = "INTEGER DEFAULT 0")
    private Integer savedClothesCount;

    @Setter
    @Column(columnDefinition = "BIT DEFAULT 0")
    private Boolean isShared;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Commission commission;

    @Builder.Default
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Schedule> scheduleList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<MemberChat> memberChatList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<CommissionClothes> commissionClothesList = new ArrayList<>();

    public void addSchedule(Schedule schedule){
        scheduleList.add(schedule);
    }

    public void addMemberChat(MemberChat chat){ memberChatList.add(chat); }

  
}