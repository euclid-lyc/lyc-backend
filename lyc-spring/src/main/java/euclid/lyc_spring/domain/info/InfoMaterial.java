package euclid.lyc_spring.domain.info;

import euclid.lyc_spring.domain.enums.Material;
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
public class InfoMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Material material;

    @Column(nullable = false)
    private Boolean isPrefer;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "info_id", nullable = false)
    private Info info;

    protected InfoMaterial() {}

    @Builder
    public InfoMaterial(Info info, Material material, Boolean isPrefer) {
        this.info = info;
        this.material = material;
        this.isPrefer = isPrefer;
    }
}