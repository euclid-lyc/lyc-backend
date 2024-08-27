package euclid.lyc_spring.domain.chat;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Entity
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(columnDefinition = "text")
    private String memo;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_id", nullable = false)
    private Chat chat;

    protected Schedule(){}

    @Builder
    public Schedule(LocalDate date, String memo) {
        this.date = date;
        this.memo = memo;
    }
}
