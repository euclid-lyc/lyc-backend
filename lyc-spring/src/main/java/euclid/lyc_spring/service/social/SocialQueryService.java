package euclid.lyc_spring.service.social;

import euclid.lyc_spring.dto.response.MemberDTO;

import java.util.List;

public interface SocialQueryService {

/*-------------------------------------------------- 회원 팔로우 및 팔로잉 --------------------------------------------------*/

    List<MemberDTO.FollowDTO> getFollowerList(Long memberId);
    List<MemberDTO.FollowDTO> getFollowingList(Long memberId);

/*-------------------------------------------------- 인기 디렉터 --------------------------------------------------*/

    List<MemberDTO.TodayDirectorDTO> getTodayDirectorList();

/*-------------------------------------------------- 프로필 --------------------------------------------------*/

    MemberDTO.MemberInfoDTO getMemberInfoDTO(Long memberId);

/*-------------------------------------------------- 회원 차단 --------------------------------------------------*/

/*-------------------------------------------------- 회원 신고 --------------------------------------------------*/

}