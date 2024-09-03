package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.mapping.QSavedPosting;
import euclid.lyc_spring.domain.mapping.SavedPosting;
import euclid.lyc_spring.repository.querydsl.SavedPostingRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class SavedPostingRepositoryCustomImpl implements SavedPostingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SavedPosting> findSavedPostingsByMemberId(Long memberId, Integer pageSize, LocalDateTime cursorDateTime) {
        QSavedPosting savedPosting = QSavedPosting.savedPosting;
        return queryFactory
                .selectFrom(savedPosting)
                .where(savedPosting.member.id.eq(memberId)
                        .and(savedPosting.createdAt.before(cursorDateTime)))
                .orderBy(savedPosting.createdAt.desc(), savedPosting.id.asc())
                .limit(pageSize)
                .fetch();
    }
}
