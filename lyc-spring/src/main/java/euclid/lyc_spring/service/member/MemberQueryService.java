package euclid.lyc_spring.service.member;

import euclid.lyc_spring.dto.response.MemberDTO;

public interface MemberQueryService {

/*-------------------------------------------------- 회원정보 설정 --------------------------------------------------*/
    MemberDTO.MemberSettingInfoDTO getMemberSettingInfo();
    MemberDTO.AddressDTO getAdrress();
    MemberDTO.PushSetDTO getPushSet();
/*-------------------------------------------------- 푸시알림 설정 --------------------------------------------------*/

}
