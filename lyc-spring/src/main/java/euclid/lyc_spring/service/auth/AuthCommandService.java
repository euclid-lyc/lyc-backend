package euclid.lyc_spring.service.auth;

import euclid.lyc_spring.dto.request.RegisterDTO;
import euclid.lyc_spring.dto.request.SignRequestDTO;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.dto.response.SignDTO;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthCommandService {

/*-------------------------------------------------- 회원가입 및 탈퇴 --------------------------------------------------*/
/*-------------------------------------------------- 로그인 및 로그아웃 --------------------------------------------------*/

    SignDTO.SignInDTO signIn(SignRequestDTO.SignInDTO signInRequestDTO, HttpServletResponse response);
    MemberDTO.MemberInfoDTO join(RegisterDTO.RegisterMemberDTO registerMemberDTO);
}
