package euclid.lyc_spring.domain.mapping;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.ImageMessage;
import euclid.lyc_spring.domain.chat.TextMessage;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Entity
public class MemberChat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @OneToMany(mappedBy = "memberChat", cascade = CascadeType.ALL)
    private List<TextMessage> textMessageList;

    @OneToMany(mappedBy = "memberChat", cascade = CascadeType.ALL)
    private List<ImageMessage> imageMessageList;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;
}