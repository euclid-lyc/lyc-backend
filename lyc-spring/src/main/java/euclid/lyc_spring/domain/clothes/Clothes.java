package euclid.lyc_spring.domain.clothes;

import euclid.lyc_spring.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@EntityListeners(AuditingEntityListener.class)
public class Clothes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private java.lang.Long id;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @Column(length = 20, nullable = false)
    private String title;

    @Column(length = 100)
    private String text;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(mappedBy = "clothes", cascade = CascadeType.ALL)
    private ClothesImage clothesImage;

    @OneToOne(mappedBy = "clothes", cascade = CascadeType.ALL)
    private ClothesText clothesText;

    protected Clothes() {}

    public Clothes(Member member, String title, String text) {
        this.member = member;
        this.title = title;
        this.text = text;
    }

    //=== add Methods ===//
    public void addClothesImage(ClothesImage clothesImage) {
        this.clothesImage = clothesImage;
        clothesImage.setClothes(this);
    }

    public void addClothesText(ClothesText clothesText) {
        this.clothesText = clothesText;
        clothesText.setClothes(this);
    }
}