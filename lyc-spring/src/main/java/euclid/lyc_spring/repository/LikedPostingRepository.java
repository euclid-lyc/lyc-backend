package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.mapping.LikedPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikedPostingRepository extends JpaRepository<LikedPosting, Long> {

    List<LikedPosting> findAllByMember_Id(Long memberId);
}
