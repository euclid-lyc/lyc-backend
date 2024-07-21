package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.mapping.SavedPosting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SavedPostingRepository extends JpaRepository<SavedPosting, Long> {

    List<SavedPosting> findAllByMember_Id(Long memberId);
    boolean existsByMember_IdAndPost_Id(Long memberId, Long postId);

}