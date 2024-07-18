package euclid.lyc_spring.domain.clothes;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class ClothesImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String image;

    @Column(length = 100)
    private String text;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothes_id", nullable = false)
    private Clothes clothes;

    protected ClothesImage() {}

    @Builder
    public ClothesImage(String image, String text) {
        this.image = image;
        this.text = text;
    }
}
