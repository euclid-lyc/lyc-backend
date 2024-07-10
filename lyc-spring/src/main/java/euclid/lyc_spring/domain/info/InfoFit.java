package euclid.lyc_spring.domain.info;

import euclid.lyc_spring.domain.enums.Fit;
import jakarta.persistence.*;
import lombok.Getter;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id", nullable = false)
    private Info info;
}
