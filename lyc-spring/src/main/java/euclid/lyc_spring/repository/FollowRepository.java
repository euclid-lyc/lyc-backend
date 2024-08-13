package euclid.lyc_spring.repository;


import euclid.lyc_spring.domain.Follow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    List<Follow> findByFollowingId(Long followingId);
    List<Follow> findByFollowerId(Long followerId);

    Optional<Follow> findByFollowerIdAndFollowingId(Long followerId, Long followingId);
    boolean existsByFollowerIdAndFollowingId(Long memberId, Long followingId);
}
