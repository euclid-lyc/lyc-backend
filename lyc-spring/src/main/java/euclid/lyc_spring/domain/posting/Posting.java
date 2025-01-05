package euclid.lyc_spring.domain.posting;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.domain.mapping.LikedPosting;
import euclid.lyc_spring.domain.mapping.SavedPosting;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Builder
@DynamicUpdate
@DynamicInsert
@Check(constraints = "likes >= 0")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Posting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private Double minTemp;

    @Column(nullable = false)
    private Double maxTemp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Style style;

    @Setter
    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long likes;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @Column(columnDefinition = "text")
    private String content;

    @Setter
    @OneToOne(mappedBy = "review", cascade = CascadeType.ALL)
    private Commission commission;

    @Builder.Default
    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
    private List<Image> imageList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
    private List<LikedPosting> likedPostingList = new ArrayList<>();

    @Builder.Default
    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
    private List<SavedPosting> savedPostingList = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member toMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false, foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private Member writer;


    public void addImage(Image image) {
        imageList.add(image);
    }

    public void addSavedPosting(SavedPosting savedPosting) {
        savedPostingList.add(savedPosting);
    }

    public void removeSavedPosting(SavedPosting savedPosting) {
        savedPostingList.remove(savedPosting);
    }

    public void reloadLikes(Long likesFromLikedPosting) {
        likes = likesFromLikedPosting;
    }

}