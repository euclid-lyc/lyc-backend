package euclid.lyc_spring.service;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.domain.BlockMember;
import euclid.lyc_spring.domain.Follow;
import euclid.lyc_spring.repository.BlockMemberRepository;
import euclid.lyc_spring.repository.FollowRepository;
import euclid.lyc_spring.repository.MemberRepository;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.dto.response.MemberDTO.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private BlockMemberRepository blockMemberRepository;


    public List<TodayDirectorDTO> getTodayDirectorList() {
        return memberRepository.findAll().stream()
                .sorted(Comparator.comparing(Member::getFollower).reversed())
                .map(TodayDirectorDTO::toDTO)
                .limit(10)
                .toList();

    }

    public MemberInfoDTO getMemberInfoDTO(Long memberId) {
        Member member = memberRepository.findById(memberId).orElse(null);
        return MemberInfoDTO.toDTO(Objects.requireNonNull(member));
    }

    public List<FollowDTO> getFollowerList(Long memberId) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Follow> followers = followRepository.findByFollowerId(memberId);
        return followers.stream()
                .map(Follow::getFollower)
                .map(FollowDTO::toDTO)
                .toList();
    }

    public List<FollowDTO> getFollowingList(Long memberId) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Follow> followers = followRepository.findByFollowingId(memberId);
        return followers.stream()
                .map(Follow::getFollowing)
                .map(FollowDTO::toDTO)
                .toList();
    }

    // FOLLOW

    // 팔로우하기
    public void followMember(Long myId, Long memberId) {
        Member followingMember = memberRepository.findById(memberId).orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (followRepository.findByFollowerIdAndFollowingId(myId,memberId).isPresent()) {
            throw new RuntimeException("이미 팔로우 중입니다.");
        }

        Member followerMember = new Member(myId);
        Follow follow = new Follow(followerMember,followingMember);
        followRepository.save(follow);
    }

    // 언팔로우하기
    public void unfollowMember(Long myId, Long memberId) {

        Follow follow = followRepository.findByFollowerIdAndFollowingId(myId,memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.NOT_FOLLOWING));

        followRepository.delete(follow);
    }

    // BLOCKED

    // 차단하기
    public void blockMember(Long memberId, Long blockMemberId) {
        Member blockMember = memberRepository.findById(blockMemberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        if (blockMemberRepository.findByMember_IdAndBlock_Id(memberId,blockMemberId).isPresent()) {
            throw new MemberHandler(ErrorStatus.MEMBER_ALREADY_BLOCKED);
        }

        Member member = new Member(memberId);
        BlockMember blockMemberRelation = new BlockMember(member, blockMember);
        blockMemberRepository.save(blockMemberRelation);
    }

    // 차단 해제하기
    public void unblockMember(Long memberId, Long blockMemberId) {
        BlockMember blockMemberRelation = blockMemberRepository.findByMember_IdAndBlock_Id(memberId, blockMemberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_BLOCKED));

        blockMemberRepository.delete(blockMemberRelation);
    }
}