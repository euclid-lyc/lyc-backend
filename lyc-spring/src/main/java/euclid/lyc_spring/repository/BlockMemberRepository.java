package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.BlockMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BlockMemberRepository extends JpaRepository<BlockMember, Long> {
    // 유저 차단한 멤버 목록 조회
    List<BlockMember> findByMemberId(Long memberId);

    // 유저를 차단한 멤버 목록 조회
    List<BlockMember> findByBlockMemberId(Long blockMemberId);

    // 차단 관계 조회 (유저가 다른 멤버를 차단했는지)
    Optional<BlockMember> findByMemberIdAndBlockMemberId(Long memberId, Long blockMemberId);

    // 차단 관계 삭제
    void deleteByMemberIdAndBlockMemberId(Long memberId, Long blockId);
}