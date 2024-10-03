package euclid.lyc_spring.service.mail;

import euclid.lyc_spring.dto.request.VerificationRequestDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface MailService {

    void checkEmail(VerificationRequestDTO.FindIdDTO findIdDTO);

    void checkEmail(VerificationRequestDTO.FindPwDTO findPwDTO);

    void checkEmail(VerificationRequestDTO.SignUpDTO signUpDTO);

    void sendMailToFindId(HttpServletRequest request, HttpServletResponse response, String email);

    void sendMailToFindPw(HttpServletRequest request, HttpServletResponse response, String email, String loginId);

    void sendMailToSignUp(HttpServletRequest request, HttpServletResponse response, String email);
}
