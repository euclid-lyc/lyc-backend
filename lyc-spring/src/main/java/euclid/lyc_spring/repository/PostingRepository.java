package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.posting.Posting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostingRepository extends JpaRepository<Posting, Long> {

    List<Posting> findByToMember_Id(Long memberId);
    List<Posting> findByFromMember_Id(Long memberId);
}
