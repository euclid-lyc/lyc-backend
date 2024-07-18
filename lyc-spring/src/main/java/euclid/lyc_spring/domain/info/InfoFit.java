package euclid.lyc_spring.domain.info;

import euclid.lyc_spring.domain.enums.Fit;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class InfoFit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Fit fit;

    @Column(nullable = false)
    private Boolean isPrefer;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id", nullable = false)
    private Info info;

    protected InfoFit() {}

    @Builder
    public InfoFit(Fit fit, Boolean isPrefer) {
        this.fit = fit;
        this.isPrefer = isPrefer;
    }
}
