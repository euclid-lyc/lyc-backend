package euclid.lyc_spring.domain.posting;


import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.domain.mapping.LikedPosting;
import euclid.lyc_spring.domain.mapping.SavedPosting;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Check;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Getter
@Entity
@Check(constraints = "likes >= 0")
public class Posting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private Short minTemp;

    @Column(nullable = false)
    private Short maxTemp;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Style style;

    @Column(columnDefinition = "BIGINT DEFAULT 0")
    private Long likes;

    @CreatedDate
    @Column
    private LocalDateTime createdAt;

    @Column(columnDefinition = "text")
    private String content;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
    private List<Image> imageList;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
    private List<LikedPosting> likedPostingList;

    @OneToMany(mappedBy = "posting", cascade = CascadeType.ALL)
    private List<SavedPosting> savedPostingList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_id", nullable = false)
    private Member fromMember;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_id", nullable = false)
    private Member toMember;

    //=== Methods ===//
    public void reloadLikes(Long likesFromLikedPosting) {
        likes = likesFromLikedPosting;
    }
}
