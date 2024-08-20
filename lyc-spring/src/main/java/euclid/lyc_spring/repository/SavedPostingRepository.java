package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.mapping.SavedPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SavedPostingRepository extends JpaRepository<SavedPosting, Long> {

    List<SavedPosting> findAllByMemberId(Long memberId);
    boolean existsByMemberIdAndPostingId(Long memberId, Long postingId);
    Optional<SavedPosting> findByMemberIdAndPostingId(Long memberId, Long postingId);
}