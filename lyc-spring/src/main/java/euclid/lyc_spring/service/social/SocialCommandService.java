package euclid.lyc_spring.service.social;

import euclid.lyc_spring.dto.response.MemberDTO;

public interface SocialCommandService {


/*-------------------------------------------------- 회원 팔로우 및 팔로잉 --------------------------------------------------*/

    MemberDTO.MemberInfoDTO followMember(Long myId);
    MemberDTO.MemberInfoDTO unfollowMember(Long myId);

/*-------------------------------------------------- 인기 디렉터 --------------------------------------------------*/

/*-------------------------------------------------- 프로필 --------------------------------------------------*/

/*-------------------------------------------------- 회원 차단 --------------------------------------------------*/

    MemberDTO.MemberInfoDTO blockMember(Long myId, Long memberId);
    MemberDTO.MemberInfoDTO unblockMember(Long myId, Long memberId);

/*-------------------------------------------------- 회원 신고 --------------------------------------------------*/

}
