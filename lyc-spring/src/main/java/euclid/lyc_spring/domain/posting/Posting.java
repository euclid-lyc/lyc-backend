package euclid.lyc_spring.domain.posting;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.domain.mapping.LikedPosting;
import euclid.lyc_spring.domain.mapping.SavedPosting;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
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
@DynamicUpdate
@DynamicInsert
@Check(constraints = "likes >= 0")
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

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
    private List<Image> imageList;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
    private List<LikedPosting> likedPostingList;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
    private List<SavedPosting> savedPostingList;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id", nullable = false)
    private Member fromMember;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id", nullable = false)
    private Member toMember;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id", nullable = false)
    private Member writer;

    protected Posting() {}

    @Builder
    public Posting(Double minTemp, Double maxTemp, Style style, Long likes, String content,
                   Member fromMember, Member toMember, Member writer) {
        this.minTemp = minTemp;
        this.maxTemp = maxTemp;
        this.style = style;
        this.likes = likes;
        this.content = content;
        this.fromMember = fromMember;
        this.toMember = toMember;
        this.writer = writer;
        this.imageList = new ArrayList<>();
        this.likedPostingList = new ArrayList<>();
        this.savedPostingList = new ArrayList<>();
    }

    //=== add Methods ===//
    public void addImage(Image image) {
        imageList.add(image);
        image.setPosting(this);
    }

    public void addSavedPosting(SavedPosting savedPosting) {
        savedPostingList.add(savedPosting);
        savedPosting.setPosting(this);
    }

    //=== remove Methods==//
    public void removeSavedPosting(SavedPosting savedPosting) {
        savedPostingList.remove(savedPosting);
    }

    public void reloadLikes(Long likesFromLikedPosting) {
        likes = likesFromLikedPosting;
    }

}