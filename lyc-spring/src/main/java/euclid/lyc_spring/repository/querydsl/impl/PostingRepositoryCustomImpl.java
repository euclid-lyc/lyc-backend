package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.domain.posting.QPosting;
import euclid.lyc_spring.repository.querydsl.PostingRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
public class PostingRepositoryCustomImpl implements PostingRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Posting> findCoordiesByFromMemberId(Long memberId, Integer pageSize, LocalDateTime cursorDateTime) {
        QPosting posting = QPosting.posting;
        return queryFactory
                .selectFrom(posting)
                .where(posting.fromMember.id.eq(memberId)
                        .and(posting.createdAt.before(cursorDateTime)))
                .orderBy(posting.createdAt.desc(), posting.id.desc())
                .limit(pageSize)
                .fetch();
    }

    @Override
    public List<Posting> findReviewsByToMemberId(Long memberId, Integer pageSize, LocalDateTime cursorDateTime) {
        QPosting posting = QPosting.posting;
        return queryFactory
                .selectFrom(posting)
                .where(posting.toMember.id.eq(memberId)
                        .and(posting.fromMember.id.ne(memberId))
                        .and(posting.createdAt.before(cursorDateTime)))
                .orderBy(posting.createdAt.desc(), posting.id.desc())
                .limit(pageSize)
                .fetch();
    }
}
