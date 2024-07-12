package euclid.lyc_spring.domain.clothes;

import euclid.lyc_spring.domain.Member;
import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;
import java.util.List;

@Getter
@Entity
public class Clothes {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @CreatedDate
    @Column
    private Date createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(mappedBy = "clothes", cascade = CascadeType.ALL)
    private ClothesImage clothesImage;

    @OneToOne(mappedBy = "clothes", cascade = CascadeType.ALL)
    private ClothesText clothesText;

}
