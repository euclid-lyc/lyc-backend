package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.SearchHandler;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.QMember;
import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.domain.info.QInfo;
import euclid.lyc_spring.domain.info.QInfoStyle;
import euclid.lyc_spring.dto.response.SearchResponseDTO;
import euclid.lyc_spring.repository.querydsl.MemberRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class MemberRepositoryCustomImpl implements MemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<SearchResponseDTO.MemberPreviewDTO> searchDirectorByGenMode(String term) {

        QMember member = QMember.member;

        return queryFactory.selectFrom(member)
                .where(member.loginId.eq(term)
                        .or(member.nickname.eq(term)))
                .fetch().stream().map(SearchResponseDTO.MemberPreviewDTO::toDTO)
                .toList();
    }

    @Override
    public List<SearchResponseDTO.MemberKeywordPreviewDTO> searchDirectorByKeywordMode(List<String> term, String orderType) {
        QMember member = QMember.member;
        QInfo info = QInfo.info;
        QInfoStyle infoStyle = QInfoStyle.infoStyle;

        // 스타일 문자열을 Style Enum으로 변환
        List<Style> styleEnums = term.stream()
                .map(Style::valueOf)
                .toList();

        // 인기도 정렬
        if(Objects.equals(orderType, "popularity")) {
            List<Member> members = queryFactory
                    .selectFrom(member)
                    .join(member.info, info)
                    .join(info.infoStyleList, infoStyle)
                    .where(infoStyle.isPrefer.eq(true)
                            .and(infoStyle.style.in(styleEnums))) // 변환된 Enum 리스트로 비교
                    .groupBy(member.id)
                    .orderBy(infoStyle.style.count().desc(), member.popularity.desc())
                    .fetch();

            return members.stream()
                    .map(SearchResponseDTO.MemberKeywordPreviewDTO::toDTO)
                    .collect(Collectors.toList());

        } else if(Objects.equals(orderType, "recentAct")){ // 최근활동순 정렬
            List<Member> members = queryFactory
                    .selectFrom(member)
                    .join(member.info, info)
                    .join(info.infoStyleList, infoStyle)
                    .where(infoStyle.isPrefer.eq(true)
                            .and(infoStyle.style.in(styleEnums))) // 변환된 Enum 리스트로 비교
                    .groupBy(member.id)
                    .orderBy(infoStyle.style.count().desc(), member.lastLoginAt.desc())
                    .fetch();

            return members.stream()
                    .map(SearchResponseDTO.MemberKeywordPreviewDTO::toDTO)
                    .collect(Collectors.toList());
        }

        throw new SearchHandler(ErrorStatus.CATEGORY_NOT_CORRECT);
    }
}
