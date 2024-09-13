package euclid.lyc_spring.domain.posting;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@DynamicUpdate
@DynamicInsert
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String image;

    @OneToMany(mappedBy = "image", cascade = CascadeType.ALL)
    private List<ImageUrl> imageUrlList;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "posting_id", nullable = false)
    private Posting posting;

    protected Image() {}

    @Builder
    public Image(String image, Posting posting) {
        this.image = image;
        this.posting = posting;
        this.imageUrlList = new ArrayList<>();
    }


    //=== Methods ===//
    public void addImageUrl(ImageUrl imageUrl) {
        imageUrlList.add(imageUrl);
        imageUrl.setImage(this);
    }
}