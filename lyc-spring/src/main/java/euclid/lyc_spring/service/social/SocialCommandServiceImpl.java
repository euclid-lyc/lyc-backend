package euclid.lyc_spring.service.social;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.BlockMember;
import euclid.lyc_spring.domain.Follow;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.repository.BlockMemberRepository;
import euclid.lyc_spring.repository.FollowRepository;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class SocialCommandServiceImpl implements SocialCommandService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final BlockMemberRepository blockMemberRepository;

/*-------------------------------------------------- 회원 팔로우 및 팔로잉 --------------------------------------------------*/

    @Override
    public MemberDTO.MemberInfoDTO followMember(Long memberId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Member following = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (Objects.equals(loginMember.getId(), memberId)){
            throw new MemberHandler(ErrorStatus.FORBIDDEN);
        } else if (followRepository.findByFollowerIdAndFollowingId(loginMember.getId(),memberId).isPresent()) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_FOLLOWING);
        } else if (blockMemberRepository.findByMemberIdAndBlockMemberId(loginMember.getId(), memberId).isPresent()) {
            throw new MemberHandler(ErrorStatus.BLOCKING_MEMBER);
        } else if (blockMemberRepository.findByMemberIdAndBlockMemberId(memberId, loginMember.getId()).isPresent()) {
            throw new MemberHandler(ErrorStatus.BLOCKED_MEMBER);
        }

        Member follower = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Follow follow = new Follow(follower,following);

        following.reloadFollower(following.getFollower()+1);
        follower.reloadFollowing(follower.getFollowing()+1);

        followRepository.save(follow);

        return MemberDTO.MemberInfoDTO.toDTO(following);
    }

    @Override
    public MemberDTO.MemberInfoDTO unfollowMember(Long memberId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member loginMember = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (Objects.equals(loginMember.getId(), memberId)){
            throw new MemberHandler(ErrorStatus.FORBIDDEN);
        } else if (followRepository.findByFollowerIdAndFollowingId(loginMember.getId(),memberId).isEmpty()) {
            throw new MemberHandler(ErrorStatus.NOT_FOLLOWING);
        }
        Follow follow = followRepository.findByFollowerIdAndFollowingId(loginMember.getId(),memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.NOT_FOLLOWING));

        Member followingMember = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member member = memberRepository.findById(loginMember.getId())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        followingMember.reloadFollower(followingMember.getFollower()-1);
        member.reloadFollowing(member.getFollowing()-1);

        followRepository.delete(follow);
        return MemberDTO.MemberInfoDTO.toDTO(followingMember);
    }

/*-------------------------------------------------- 인기 디렉터 --------------------------------------------------*/

/*-------------------------------------------------- 프로필 --------------------------------------------------*/

/*-------------------------------------------------- 회원 차단 --------------------------------------------------*/

    @Override
    public MemberDTO.MemberInfoDTO blockMember(Long memberId, Long blockMemberId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (Objects.equals(memberId, blockMemberId)){
            throw new MemberHandler(ErrorStatus.FORBIDDEN);
        } else if (blockMemberRepository.findByMemberIdAndBlockMemberId(memberId, blockMemberId).isPresent()) {
            throw new MemberHandler(ErrorStatus.BLOCKING_MEMBER);
        }

        Member blockMember = memberRepository.findById(blockMemberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 차단 관계 생성
        BlockMember blockMemberRelation = new BlockMember(member, blockMember);
        blockMemberRepository.save(blockMemberRelation);

        // 팔로우 관계 삭제
        if(followRepository.existsByFollowerIdAndFollowingId(memberId, blockMemberId)){
            Follow follow = followRepository.findByFollowerIdAndFollowingId(memberId, blockMemberId).orElseThrow();
            followRepository.delete(follow);
            member.reloadFollowing(member.getFollowing()-1);
            blockMember.reloadFollower(blockMember.getFollower()-1);
        }
        if(followRepository.findByFollowerIdAndFollowingId(blockMemberId, memberId).isPresent()){
            Follow follow = followRepository.findByFollowerIdAndFollowingId(blockMemberId, memberId).orElseThrow();
            followRepository.delete(follow);
            blockMember.reloadFollowing(blockMember.getFollowing()-1);
            member.reloadFollower(member.getFollower()-1);
        }

        return MemberDTO.MemberInfoDTO.toDTO(blockMember);
    }

    @Override
    public MemberDTO.MemberInfoDTO unblockMember(Long memberId, Long blockMemberId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (blockMemberRepository.findByMemberIdAndBlockMemberId(memberId, blockMemberId).isEmpty()) {
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_BLOCKING);
        }

        Member blockMember = memberRepository.findById(blockMemberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        BlockMember blockMemberRelation = blockMemberRepository.findByMemberIdAndBlockMemberId(memberId, blockMemberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_BLOCKING));

        blockMemberRepository.delete(blockMemberRelation);

        return MemberDTO.MemberInfoDTO.toDTO(blockMember);
    }

    /*-------------------------------------------------- 회원 신고 --------------------------------------------------*/

}
