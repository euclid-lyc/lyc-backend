package euclid.lyc_spring.domain.info;

import euclid.lyc_spring.domain.enums.Style;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class InfoStyle {

    public InfoStyle(Info info, Style style, Boolean isPrefer) {
        this.info = info;
        this.style = style;
        this.isPrefer = isPrefer;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Style style;

    @Column(nullable = false)
    private Boolean isPrefer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id", nullable = false)
    private Info info;

    public InfoStyle() {
        super();
    }
}
