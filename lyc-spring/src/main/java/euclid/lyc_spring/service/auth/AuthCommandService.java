package euclid.lyc_spring.service.auth;

import euclid.lyc_spring.dto.request.RegisterDTO;
import euclid.lyc_spring.dto.request.SignRequestDTO;
import euclid.lyc_spring.dto.request.VerificationRequestDTO;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.dto.response.SignDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthCommandService {

/*-------------------------------------------------- 회원가입 및 탈퇴 --------------------------------------------------*/

    void verifyCode(HttpServletRequest request, String code);
    MemberDTO.MemberInfoDTO join(HttpServletRequest request, RegisterDTO.RegisterMemberDTO registerMemberDTO, String imageUrl);
    MemberDTO.MemberPreviewDTO withdraw();

/*-------------------------------------------------- 로그인 및 로그아웃 --------------------------------------------------*/

    SignDTO.SignInDTO signIn(SignRequestDTO.SignInDTO signInRequestDTO, HttpServletResponse response);
    SignDTO.SignOutDTO signOut(HttpServletRequest request);
    MemberDTO.MemberPreviewDTO findId(HttpServletRequest request, VerificationRequestDTO.IdVerificationDTO memberAuthDTO);
    MemberDTO.MemberPreviewDTO updatePw(HttpServletRequest request, VerificationRequestDTO.PwVerificationDTO pwVerificationDTO);
}
