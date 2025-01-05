package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.domain.PointUsage;

import java.time.LocalDateTime;
import java.util.List;

public interface PointUsageRepositoryCustom {

    List<PointUsage> findMemberPointUsages(Long memberId, Integer pageSize, LocalDateTime cursorCreatedAt);
}
