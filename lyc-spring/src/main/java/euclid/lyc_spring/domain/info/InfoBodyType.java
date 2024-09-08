package euclid.lyc_spring.domain.info;

import euclid.lyc_spring.domain.enums.BodyType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Getter
@Entity
@DynamicUpdate
@DynamicInsert
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

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id", nullable = false)
    private Info info;

    protected InfoBodyType() {}

    @Builder
    public InfoBodyType(BodyType bodyType, Boolean isGood) {
        this.bodyType = bodyType;
        this.isGood = isGood;
    }
}