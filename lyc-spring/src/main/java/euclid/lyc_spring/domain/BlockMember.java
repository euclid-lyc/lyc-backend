package euclid.lyc_spring.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
public class BlockMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_member_id", nullable = false)
    private Member blockMember;

}
