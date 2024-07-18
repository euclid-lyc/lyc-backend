package euclid.lyc_spring.domain.info;

import euclid.lyc_spring.domain.enums.Style;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class InfoStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Style style;

    @Column(nullable = false)
    private Boolean isPrefer;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id", nullable = false)
    private Info info;

    protected InfoStyle() {}

    @Builder
    public InfoStyle(Style style, Boolean isPrefer) {
        this.style = style;
        this.isPrefer = isPrefer;
    }


}
