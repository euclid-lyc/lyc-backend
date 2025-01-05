package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.PointUsage;
import euclid.lyc_spring.domain.QMember;
import euclid.lyc_spring.domain.QPointUsage;
import euclid.lyc_spring.repository.querydsl.PointUsageRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class PointUsageRepositoryCustomImpl implements PointUsageRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PointUsage> findMemberPointUsages(Long memberId, Integer pageSize, LocalDateTime cursorCreatedAt) {

        QPointUsage pointUsage = QPointUsage.pointUsage;

        BooleanBuilder whereClause = new BooleanBuilder()
                .and(pointUsage.member.id.eq(memberId));

        if (cursorCreatedAt != null) {
            whereClause.and(pointUsage.createdAt.before(cursorCreatedAt));
        }

        return queryFactory
                .selectFrom(pointUsage)
                .where(whereClause)
                .orderBy(pointUsage.createdAt.desc(), pointUsage.id.asc())
                .limit(pageSize)
                .fetch();
    }
}
