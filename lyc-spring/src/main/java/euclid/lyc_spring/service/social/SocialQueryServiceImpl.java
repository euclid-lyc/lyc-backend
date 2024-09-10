package euclid.lyc_spring.service.social;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.info.Info;
import euclid.lyc_spring.dto.response.InfoResponseDTO;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.repository.BlockMemberRepository;
import euclid.lyc_spring.repository.FollowRepository;
import euclid.lyc_spring.repository.InfoRepository;
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
    private final InfoRepository infoRepository;

/*-------------------------------------------------- 회원 팔로우 및 팔로잉 --------------------------------------------------*/

    @Override
    public MemberDTO.MemberIntroListDTO getFollowerList(Long memberId, Integer pageSize, String cursorNickname) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Member> followers = followRepository.findFollowers(memberId, pageSize, cursorNickname);
        return MemberDTO.MemberIntroListDTO.toDTO(followers);
    }

    @Override
    public MemberDTO.MemberIntroListDTO getFollowingList(Long memberId, Integer pageSize, String cursorNickname) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Member> followings = followRepository.findFollowings(memberId, pageSize, cursorNickname);
        return MemberDTO.MemberIntroListDTO.toDTO(followings);
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

    @Override
    public InfoResponseDTO.AllInfoDTO getStyleInfo(Long memberId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Info info = infoRepository.findByMemberId(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_INFO_NOT_FOUND));

        // 비공개 설정 시 스타일 정보 반환 X
        if (!info.getIsPublic()) {
            throw new MemberHandler(ErrorStatus.MEMBER_STYLE_INFO_IS_PRIVATE);
        }

        return InfoResponseDTO.AllInfoDTO.toDTO(info);
    }

/*-------------------------------------------------- 회원 차단 --------------------------------------------------*/

    @Override
    public MemberDTO.MemberIntroListDTO getAllBlockMembers(Integer pageSize, Long blockMemberId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Member> blockMembers = blockMemberRepository.findBlockMembers(member.getId(), blockMemberId, pageSize);
        return MemberDTO.MemberIntroListDTO.toDTO(blockMembers);
    }

/*-------------------------------------------------- 회원 신고 --------------------------------------------------*/

}
