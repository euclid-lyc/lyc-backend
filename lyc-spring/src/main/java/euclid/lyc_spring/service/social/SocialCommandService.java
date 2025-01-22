package euclid.lyc_spring.service.social;

import euclid.lyc_spring.dto.request.InfoDTO;
import euclid.lyc_spring.dto.request.MemberRequestDTO;
import euclid.lyc_spring.dto.response.InfoResponseDTO;
import euclid.lyc_spring.dto.response.MemberDTO;

public interface SocialCommandService {


/*-------------------------------------------------- 회원 팔로우 및 팔로잉 --------------------------------------------------*/

    MemberDTO.MemberInfoDTO followMember(Long myId);
    MemberDTO.MemberInfoDTO unfollowMember(Long myId);

/*-------------------------------------------------- 인기 디렉터 --------------------------------------------------*/

/*-------------------------------------------------- 프로필 --------------------------------------------------*/

    InfoResponseDTO.AllInfoDTO updateStyleInfo(InfoDTO.StyleInfoDTO styleInfoDTO);

/*-------------------------------------------------- 회원 차단 --------------------------------------------------*/

    MemberDTO.MemberInfoDTO blockMember(Long blockMemberId);
    MemberDTO.MemberInfoDTO unblockMember(Long blockMemberId);

/*-------------------------------------------------- 회원 신고 --------------------------------------------------*/

    MemberDTO.MemberProfileDTO reportMember(Long reportedMemberId, MemberRequestDTO.ReportDTO reportDTO);

}
