package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.QCommission;
import euclid.lyc_spring.domain.enums.CommissionStatus;
import euclid.lyc_spring.repository.querydsl.CommissionRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class CommissionRepositoryCustomImpl implements CommissionRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Commission> findCommissionsByDirectorId(Long directorId, Integer pageSize, LocalDateTime cursorDateTime) {

        QCommission commission = QCommission.commission;

        return queryFactory
                .selectFrom(commission)
                .where(commission.director.id.eq(directorId)
                        .and(commission.status.eq(CommissionStatus.REQUIRED))
                        .and(commission.createdAt.before(cursorDateTime)))
                .orderBy(commission.createdAt.desc(), commission.id.desc())
                .limit(pageSize)
                .fetch();
    }

    @Override
    public List<Commission> findUnreviewedCommissions(Integer pageSize, LocalDateTime cursorDateTime, Long cursorId) {

        QCommission commission = QCommission.commission;
        BooleanBuilder whereClause = getWhereClause(cursorDateTime, cursorId, commission);

        return queryFactory
                .selectFrom(commission)
                .where(whereClause)
                .orderBy(commission.createdAt.desc(), commission.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private static BooleanBuilder getWhereClause(LocalDateTime cursorDateTime, Long cursorId, QCommission commission) {

        LocalDateTime stdDateTime = LocalDateTime.now().minusDays(30);
        BooleanBuilder whereClause = new BooleanBuilder()
                .and(commission.review.isNull())                            // review가 아직 작성되지 않은 의뢰
                .and(commission.finishedAt.after(stdDateTime));             // 최근 30일 이내에 종료된 의뢰

        if (cursorDateTime != null & cursorId != null) {
            whereClause.and(commission.finishedAt.before(cursorDateTime)    // cursorDateTime 이전에 종료된 의뢰
                    .or(commission.finishedAt.eq(cursorDateTime)
                            .and(commission.id.lt(cursorId))));             // cursorDateTime에 종료된 의뢰 중 id가 cursorId보다 작은 의뢰
        }
        return whereClause;
    }
}
