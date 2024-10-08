package euclid.lyc_spring.domain.clothes;

import euclid.lyc_spring.domain.enums.Fit;
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
public class ClothesText {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Material material;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Fit fit;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothes_id", nullable = false)
    private Clothes clothes;

    protected ClothesText() {}

    @Builder
    public ClothesText(Material material, Fit fit) {
        this.material = material;
        this.fit = fit;
    }
}
