package euclid.lyc_spring.domain.clothes;

import jakarta.persistence.*;
import lombok.Getter;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "clothes_id", nullable = false)
    private Clothes clothes;
}
