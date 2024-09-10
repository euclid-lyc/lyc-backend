package euclid.lyc_spring.service.social;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Follow;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.repository.BlockMemberRepository;
import euclid.lyc_spring.repository.FollowRepository;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class SocialQueryServiceImpl implements SocialQueryService {

    private final MemberRepository memberRepository;
    private final FollowRepository followRepository;
    private final BlockMemberRepository blockMemberRepository;

/*-------------------------------------------------- 회원 팔로우 및 팔로잉 --------------------------------------------------*/

    @Override
    public List<MemberDTO.FollowDTO> getFollowerList(Long memberId, Integer pageSize, String cursorNickname) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Member> followers = followRepository.findFollowers(memberId, pageSize, cursorNickname);
        return followers.stream()
                .map(MemberDTO.FollowDTO::toDTO)
                .toList();
    }

    @Override
    public List<MemberDTO.FollowDTO> getFollowingList(Long memberId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

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
    public MemberDTO.TodayDirectorListDTO getPopularDirectors(Integer pageSize, Long followerCount) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<MemberDTO.TodayDirectorDTO> popularDirectors = followRepository.findPopularDirectors(pageSize, followerCount).stream()
                .map(followerCountDTO -> {
                    Member member = memberRepository.findById(followerCountDTO.getMemberId())
                            .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
                    return MemberDTO.TodayDirectorDTO.toDTO(member, followerCountDTO.getFollowerCount());
                })
                .toList();

        return MemberDTO.TodayDirectorListDTO.toDTO(popularDirectors);
    }

/*-------------------------------------------------- 프로필 --------------------------------------------------*/

    @Override
    public MemberDTO.MemberInfoDTO getMemberInfoDTO(Long memberId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return MemberDTO.MemberInfoDTO.toDTO(member);
    }

/*-------------------------------------------------- 회원 차단 --------------------------------------------------*/

/*-------------------------------------------------- 회원 신고 --------------------------------------------------*/

}
