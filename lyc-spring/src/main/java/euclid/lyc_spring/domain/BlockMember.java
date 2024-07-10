package euclid.lyc_spring.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class BlockMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "block_member_id", nullable = false)
    private Member blockMember;

}
