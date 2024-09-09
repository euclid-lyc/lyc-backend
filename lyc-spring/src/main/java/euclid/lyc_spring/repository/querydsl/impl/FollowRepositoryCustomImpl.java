package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.QFollow;
import euclid.lyc_spring.domain.QMember;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.repository.querydsl.FollowRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class FollowRepositoryCustomImpl implements FollowRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<MemberDTO.FollowerCountDTO> findPopularDirectors(Integer pageSize, Long followerCount) {

        QMember member = QMember.member;
        QFollow follow = QFollow.follow;

        BooleanBuilder havingClause = new BooleanBuilder();

        if (followerCount != null) {
            havingClause.and(follow.count().lt(followerCount));
        }

        return queryFactory.select(Projections.constructor(MemberDTO.FollowerCountDTO.class,
                        member.id,
                        follow.count()))
                .from(follow)
                .join(follow.following, member)
                .groupBy(member.id)
                .orderBy(follow.count().desc(), member.id.asc()) // count(*)를 내림차순 정렬
                .having(havingClause) // cursorCount보다 팔로워가 적은 것만 반환
                .limit(pageSize)
                .fetch();
    }
}
