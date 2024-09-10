package euclid.lyc_spring.service.member;

import euclid.lyc_spring.dto.request.MemberRequestDTO;
import euclid.lyc_spring.dto.request.VerificationRequestDTO;
import euclid.lyc_spring.dto.response.MemberDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface MemberCommandService {

/*-------------------------------------------------- 회원정보 설정 --------------------------------------------------*/
    MemberDTO.MemberSettingInfoDTO updateMemberInfo(MemberRequestDTO.MemberSettingInfoDTO infoDTO);
    MemberDTO.AddressDTO updateAddress(MemberRequestDTO.AddressReqDTO addressReqDTO);
    MemberDTO.MemberPreviewDTO updateLoginPw(HttpServletRequest request, VerificationRequestDTO.ChangePasswordDTO passwordDTO);
/*-------------------------------------------------- 푸시알림 설정 --------------------------------------------------*/
    MemberDTO.PushSetDTO updatePushSet(MemberRequestDTO.PushSetDTO pushSetDTO);
}
