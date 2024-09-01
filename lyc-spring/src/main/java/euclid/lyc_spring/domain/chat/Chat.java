package euclid.lyc_spring.domain.chat;

import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.mapping.MemberChat;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
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
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Commission commission;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<Schedule> scheduleList;

    @OneToMany(mappedBy = "chat", cascade = CascadeType.ALL)
    private List<MemberChat> memberChatList;

    protected Chat(){}

    @Builder
    public Chat(LocalDateTime createdAt, LocalDateTime updatedAt, Commission commission) {
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.commission = commission;
        this.scheduleList = new ArrayList<>();
        this.memberChatList = new ArrayList<>();
    }

    public void addScheduleList(Schedule schedule){
        scheduleList.add(schedule);
        schedule.setChat(this);
    }

}