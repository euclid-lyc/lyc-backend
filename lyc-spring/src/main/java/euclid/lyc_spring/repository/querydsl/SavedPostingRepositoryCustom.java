package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.domain.mapping.SavedPosting;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public interface SavedPostingRepositoryCustom {

    List<SavedPosting> findSavedPostingsByMemberId(Long memberId, Integer pageSize, LocalDateTime cursorDateTime);
}
