package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.domain.posting.Posting;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public interface PostingRepositoryCustom {

    List<Posting> findCoordiesByFromMemberId(Long id, Integer pageSize, LocalDateTime cursorDateTime);

    List<Posting> findReviewsByToMemberId(Long memberId, Integer pageSize, LocalDateTime cursorDateTime);
}
