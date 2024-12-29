package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.QBlockMember;
import euclid.lyc_spring.domain.QMember;
import euclid.lyc_spring.domain.info.QInfo;
import euclid.lyc_spring.domain.info.QInfoFit;
import euclid.lyc_spring.domain.info.QInfoStyle;
import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.SearchHandler;
import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.domain.posting.QPosting;
import euclid.lyc_spring.dto.response.InfoResponseDTO;
import euclid.lyc_spring.dto.response.PostingDTO;
import euclid.lyc_spring.repository.querydsl.PostingRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
    public List<PostingDTO.PostingImageDTO> searchPosting(String keyword, String orderType) {

        // 정렬 방식 고민해야함(최근 게시글, 인기도, 좋아요?)

        QPosting posting = QPosting.posting;

        Style style;

        try {
            // 스타일 값 유효성 검사 및 변환
            style = Style.valueOf(keyword);  // Enum 값은 대문자로 변환
        } catch (IllegalArgumentException e) {
            // 유효하지 않은 스타일 값일 경우 빈 스타일로 설정
            style = null;
        }

        BooleanBuilder builder = new BooleanBuilder();

        // 검색어가 비어있지 않을 경우 조건 추가
        if (!keyword.isEmpty()) {
            builder.or(posting.content.containsIgnoreCase(keyword));

            if (style != null) {
                builder.or(posting.style.eq(style));
            }
        }

        if(Objects.equals(orderType, "popularity")){
            return queryFactory
                    .selectFrom(posting)
                    .where(builder)
                    .orderBy(posting.fromMember.popularity.desc())
                    .fetch().stream()
                    .map(PostingDTO.PostingImageDTO::toDTO)
                    .toList();
        } else if (Objects.equals(orderType, "createdAt")) {
            return queryFactory
                    .selectFrom(posting)
                    .where(builder)
                    .orderBy(posting.createdAt.desc())
                    .fetch().stream()
                    .map(PostingDTO.PostingImageDTO::toDTO)
                    .toList();
        } else if (Objects.equals(orderType, "likes")) {
            return queryFactory
                    .selectFrom(posting)
                    .where(builder)
                    .orderBy(posting.likes.desc())
                    .fetch().stream()
                    .map(PostingDTO.PostingImageDTO::toDTO)
                    .toList();
        }

        throw new SearchHandler(ErrorStatus.CATEGORY_NOT_CORRECT);
    }

    @Override
    public List<PostingDTO.PostingScoreDTO> findPostingsForMember(InfoResponseDTO.AllInfoDTO member, Integer pageSize, Long cursorScore, Long cursorId) {

        QPosting posting = QPosting.posting;
        QMember toMember = QMember.member;
        QBlockMember blockMember = QBlockMember.blockMember;
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

        JPAQuery<PostingDTO.PostingScoreDTO> query = queryFactory
                .select(Projections.constructor(PostingDTO.PostingScoreDTO.class,
                        toMember.id,
                        posting.id,
                        totalScore
                ))
                .from(toMember)
                .join(posting).on(posting.toMember.id.eq(toMember.id))
                .join(info).on(info.member.id.eq(toMember.id))
                .join(infoStyle).on(infoStyle.info.id.eq(info.id))
                .join(infoFit).on(infoFit.info.id.eq(info.id));

        // blockMember가 존재하는 경우에만 join 및 조건 추가
        if (existsBlockMemberData(blockMember, member.getMemberId())) {
            query.join(blockMember).on(blockMember.member.id.eq(member.getMemberId()));
            whereClause.and(blockMember.blockedMember.id.ne(posting.writer.id))
                    .and(blockMember.blockedMember.id.ne(posting.fromMember.id))
                    .and(blockMember.blockedMember.id.ne(posting.toMember.id));
        }

        return query
                .where(whereClause)
                .groupBy(toMember.id, posting.id)
                .orderBy(totalScore.desc(), posting.id.desc())
                .limit(pageSize)
                .fetch();
    }

    private BooleanBuilder getWhereClause(
            Long cursorScore,
            Long cursorId,
            QInfoFit infoFit,
            QInfoStyle infoStyle,
            NumberExpression<Long> totalScore,
            QPosting posting
    ) {
        BooleanBuilder whereClause = new BooleanBuilder()
                .and(infoFit.isPrefer.isTrue())
                .and(infoStyle.isPrefer.isTrue());

        if (cursorScore != null && cursorId != null) {
            whereClause.and(totalScore.lt(cursorScore)
                    .or(totalScore.eq(cursorScore).and(posting.id.lt(cursorId))));
        }

        return whereClause;
    }

    @Override
    public List<Posting> findPostingsByWeather(Double minTemp, Double maxTemp, Long memberId) {

        QPosting posting = QPosting.posting;
        QBlockMember blockMember = QBlockMember.blockMember;

        JPAQuery<Posting> query = queryFactory.selectFrom(posting);

        BooleanBuilder whereClause = new BooleanBuilder()
                .and(posting.minTemp.lt(minTemp+5))
                .and(posting.minTemp.gt(minTemp-5))
                .and(posting.maxTemp.lt(maxTemp+5))
                .and(posting.maxTemp.gt(maxTemp-5));

        // blockMember가 존재하는 경우에만 join 및 조건 추가
        if (existsBlockMemberData(blockMember, memberId)) {
            query.join(blockMember).on(blockMember.member.id.eq(memberId));
            whereClause.and(blockMember.blockedMember.id.ne(posting.writer.id))
                    .and(blockMember.blockedMember.id.ne(posting.fromMember.id))
                    .and(blockMember.blockedMember.id.ne(posting.toMember.id));
        }

        return queryFactory
                .selectFrom(posting)
                .where(whereClause)
                .orderBy(posting.likes.desc(), posting.createdAt.desc())
                .fetch();
    }

    private boolean existsBlockMemberData(QBlockMember blockMember, Long memberId) {
        // 데이터가 존재하는 경우 true 반환
        return queryFactory
                .selectFrom(blockMember)
                .where(blockMember.member.id.eq(memberId))
                .fetchFirst() != null;
    }


}