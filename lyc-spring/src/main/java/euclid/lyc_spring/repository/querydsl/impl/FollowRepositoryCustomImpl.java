package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.Follow;
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

        return queryFactory
                .select(Projections.constructor(MemberDTO.FollowerCountDTO.class,
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

    @Override
    public List<Member> findFollowers(Long memberId, Integer pageSize, String cursorNickname) {
        QMember follower = new QMember("follower");
        QMember following = new QMember("following");
        QFollow follow = QFollow.follow;

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(following.id.eq(memberId));

        if (cursorNickname != null) {
            whereClause.and(follower.nickname.gt(cursorNickname));
        }

        return queryFactory
                .selectFrom(follower)
                .join(follow).on(follower.id.eq(follow.follower.id))
                .join(following).on(follow.following.id.eq(following.id))
                .where(whereClause)
                .orderBy(follower.nickname.asc(), follower.id.asc())
                .limit(pageSize)
                .fetch();

    }

    @Override
    public List<Member> findFollowings(Long memberId, Integer pageSize, String cursorNickname) {

        QMember follower = new QMember("follower");
        QMember following = new QMember("following");
        QFollow follow = QFollow.follow;

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(follower.id.eq(memberId));

        if (cursorNickname != null) {
            whereClause.and(following.nickname.gt(cursorNickname));
        }

        return queryFactory
                .selectFrom(following)
                .join(follow).on(following.id.eq(follow.following.id))
                .join(follower).on(follow.follower.id.eq(follower.id))
                .where(whereClause)
                .orderBy(following.nickname.asc(), following.id.asc())
                .limit(pageSize)
                .fetch();
    }
}
