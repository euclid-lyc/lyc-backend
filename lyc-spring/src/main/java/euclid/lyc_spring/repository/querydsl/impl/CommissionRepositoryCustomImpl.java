package euclid.lyc_spring.repository.querydsl.impl;

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
}
