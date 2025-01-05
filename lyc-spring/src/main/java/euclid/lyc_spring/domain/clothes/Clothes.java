package euclid.lyc_spring.domain.clothes;

import euclid.lyc_spring.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Entity
@Builder
@DynamicUpdate
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
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

    @Column(columnDefinition = "BIT DEFAULT 0")
    private Boolean isText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member member;

    @Setter
    @OneToOne(mappedBy = "clothes", cascade = CascadeType.ALL)
    private ClothesImage clothesImage;

    @Setter
    @OneToOne(mappedBy = "clothes", cascade = CascadeType.ALL)
    private ClothesText clothesText;

}