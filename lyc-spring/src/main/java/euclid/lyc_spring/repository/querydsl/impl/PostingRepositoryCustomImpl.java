package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.SearchHandler;
import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.domain.info.QInfo;
import euclid.lyc_spring.domain.info.QInfoStyle;
import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.domain.posting.QPosting;
import euclid.lyc_spring.dto.response.PostingDTO;
import euclid.lyc_spring.repository.querydsl.PostingRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
}
