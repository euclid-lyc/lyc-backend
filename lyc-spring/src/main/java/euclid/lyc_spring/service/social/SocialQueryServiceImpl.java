package euclid.lyc_spring.service.social;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.domain.Follow;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.repository.BlockMemberRepository;
import euclid.lyc_spring.repository.FollowRepository;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SocialQueryServiceImpl implements SocialQueryService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final BlockMemberRepository blockMemberRepository;

/*-------------------------------------------------- 회원 팔로우 및 팔로잉 --------------------------------------------------*/

    @Override
    public List<MemberDTO.FollowDTO> getFollowerList(Long memberId) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Follow> followers = followRepository.findByFollowingId(memberId);
        return followers.stream()
                .map(Follow::getFollower)
                .map(MemberDTO.FollowDTO::toDTO)
                .toList();
    }

    @Override
    public List<MemberDTO.FollowDTO> getFollowingList(Long memberId) {

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Follow> followers = followRepository.findByFollowerId(memberId);
        return followers.stream()
                .map(Follow::getFollowing)
                .map(MemberDTO.FollowDTO::toDTO)
                .toList();
    }

/*-------------------------------------------------- 인기 디렉터 --------------------------------------------------*/

    @Override
    public List<MemberDTO.TodayDirectorDTO> getTodayDirectorList() {
        return memberRepository.findAll().stream()
                .sorted(Comparator.comparing(Member::getFollower).reversed())
                .map(MemberDTO.TodayDirectorDTO::toDTO)
                .limit(10)
                .toList();

    }

/*-------------------------------------------------- 프로필 --------------------------------------------------*/

    @Override
    public MemberDTO.MemberInfoDTO getMemberInfoDTO(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return MemberDTO.MemberInfoDTO.toDTO(Objects.requireNonNull(member));
    }

/*-------------------------------------------------- 회원 차단 --------------------------------------------------*/

/*-------------------------------------------------- 회원 신고 --------------------------------------------------*/

}
