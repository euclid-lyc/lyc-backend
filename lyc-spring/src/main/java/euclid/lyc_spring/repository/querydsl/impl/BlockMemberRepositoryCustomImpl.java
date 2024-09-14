package euclid.lyc_spring.repository.querydsl.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.QBlockMember;
import euclid.lyc_spring.domain.QMember;
import euclid.lyc_spring.repository.querydsl.BlockMemberRepositoryCustom;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class BlockMemberRepositoryCustomImpl implements BlockMemberRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<Member> findBlockMembers(Long memberId, Long cursorBlockMemberId, Integer pageSize) {

        QMember member = new QMember("member");
        QMember blockedMember = new QMember("blockedMember");
        QBlockMember blockMember = new QBlockMember("blockMember");

        BooleanBuilder whereClause = new BooleanBuilder();
        whereClause.and(blockMember.member.id.eq(memberId));

        if (cursorBlockMemberId != null) {
            whereClause.and(blockedMember.id.gt(cursorBlockMemberId));
        }

        return queryFactory
                .selectFrom(blockedMember)
                .join(blockMember).on(blockMember.blockMember.id.eq(blockedMember.id))
                .join(member).on(member.id.eq(blockMember.member.id))
                .where(whereClause)
                .orderBy(blockedMember.id.asc())
                .limit(pageSize)
                .fetch();
    }
}
