package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.QMember;
import euclid.lyc_spring.domain.info.QInfo;
import euclid.lyc_spring.domain.info.QInfoFit;
import euclid.lyc_spring.domain.info.QInfoStyle;
import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.domain.posting.QPosting;
import euclid.lyc_spring.dto.response.InfoResponseDTO;
import euclid.lyc_spring.dto.response.PostingDTO;
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

    @Override
    public List<PostingDTO.PostingScoreDTO> findPostingsForMember(InfoResponseDTO.AllInfoDTO member, Integer pageSize, Long cursorScore, Long cursorId) {

        QPosting posting = QPosting.posting;
        QMember toMember = QMember.member;
        QInfo info = QInfo.info;
        QInfoStyle infoStyle = QInfoStyle.infoStyle;
        QInfoFit infoFit = QInfoFit.infoFit;

        // 포스팅 스타일 일치 가산점
        NumberExpression<Long> postingStyleCase = new CaseBuilder()
                .when(posting.style.in(member.getPreferredStyle().getStyles()))
                .then(15L)
                .otherwise(0L);

        // 코디를 받은 회원의 선호 스타일 일치 가산점
        NumberExpression<Long> infoStyleCase = new CaseBuilder()
                .when(infoStyle.style.in(member.getPreferredStyle().getStyles()))
                .then(10L)
                .otherwise(0L);

        // 코디를 받은 회원의 선호 핏 일치 가산점
        NumberExpression<Long> infoFitCase = new CaseBuilder()
                .when(infoFit.fit.in(member.getPreferredFit().getFits()))
                .then(5L)
                .otherwise(0L);

        // 총점 계산
        NumberExpression<Long> totalScore = postingStyleCase
                .add(infoStyleCase)
                .add(infoFitCase)
                .subtract(info.weight.subtract(member.getSpec().getWeight()).abs())
                .subtract(info.height.subtract(member.getSpec().getHeight()).abs());

        BooleanBuilder whereClause = getWhereClause(cursorScore, cursorId, infoFit, infoStyle, totalScore, posting);

        return queryFactory
                .select(Projections.constructor(PostingDTO.PostingScoreDTO.class,
                        toMember.id,
                        posting.id,
                        totalScore//.as("total")
                ))
                .from(toMember)
                .join(posting).on(posting.toMember.id.eq(toMember.id))
                .join(info).on(info.member.id.eq(toMember.id))
                .join(infoStyle).on(infoStyle.info.id.eq(info.id))
                .join(infoFit).on(infoFit.info.id.eq(info.id))
                .where(whereClause)
                .groupBy(toMember.id, posting.id)
                .orderBy(totalScore.desc(), posting.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private static BooleanBuilder getWhereClause(Long cursorScore, Long cursorId, QInfoFit infoFit, QInfoStyle infoStyle, NumberExpression<Long> totalScore, QPosting posting) {
        BooleanBuilder whereClause = new BooleanBuilder()
                .and(infoFit.isPrefer.isTrue())
                .and(infoStyle.isPrefer.isTrue());

        if (cursorScore != null && cursorId != null) {
            whereClause.and(totalScore.lt(cursorScore)
                    .or(totalScore.eq(cursorScore).and(posting.id.lt(cursorId))));
        }

        return whereClause;
    }

}
