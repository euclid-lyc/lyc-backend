package euclid.lyc_spring.domain.mapping;

import euclid.lyc_spring.domain.Chat;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.Message;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.List;

@Getter
@Entity
public class MemberChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @OneToMany(mappedBy = "memberChat", cascade = CascadeType.ALL)
    private List<Message> messageList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
}
