package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.posting.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {

    List<Posting> findByToMemberId(Long memberId);
    List<Posting> findByFromMemberId(Long memberId);
    Optional<Posting> findByIdAndWriter(Long id, Member writer);
}