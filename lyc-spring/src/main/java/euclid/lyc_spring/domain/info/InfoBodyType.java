package euclid.lyc_spring.domain.info;

import euclid.lyc_spring.domain.enums.BodyType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class InfoBodyType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BodyType bodyType;

    @Column(nullable = false)
    private Boolean isGood;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id", nullable = false)
    private Info info;
}
