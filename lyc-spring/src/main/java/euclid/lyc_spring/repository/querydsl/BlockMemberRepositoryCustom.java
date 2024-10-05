package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.domain.Member;

import java.util.List;

public interface BlockMemberRepositoryCustom {

    List<Member> findBlockMembers(Long id, Long blockMemberId, Integer pageSize);
}
