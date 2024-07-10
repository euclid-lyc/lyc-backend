package euclid.lyc_spring.domain;

import euclid.lyc_spring.domain.enums.NotificationType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private NotificationType type;

    @Column(columnDefinition = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;
}
