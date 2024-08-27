package euclid.lyc_spring.service.mail;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MailHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.JwtGenerator;
import euclid.lyc_spring.dto.request.VerificationRequestDTO;
import euclid.lyc_spring.repository.MemberRepository;
import euclid.lyc_spring.repository.mail.VerificationCodeRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MailServiceImpl implements MailService {

    private final JavaMailSender javaMailSender;
    private final JwtGenerator jwtGenerator;

    private final MemberRepository memberRepository;
    private final VerificationCodeRepository verificationCodeRepository;

    @Value("${spring.mail.username}")
    private String sender;
    private static final String subject = "[유클리드] 이메일 확인 코드 : ";
    private static final String senderName = "유클리드(LYC)";

    @Override
    public void checkEmail(VerificationRequestDTO.FindIdDTO findIdDTO) {
        memberRepository.findByNameAndEmail(findIdDTO.getName(), findIdDTO.getEmail())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public void checkEmail(VerificationRequestDTO.FindPwDTO findPwDTO) {
        memberRepository.findByNameAndLoginIdAndEmail(findPwDTO.getName(), findPwDTO.getLoginId(), findPwDTO.getEmail())
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }

    @Override
    public void sendMailToFindId(HttpServletRequest request, HttpServletResponse response, String email) {

        String tempToken = jwtGenerator.generateTempToken(email);
        setHeader(tempToken, response);

        String verificationCode = createCode();
        MimeMessage message = createMessage(email, verificationCode);

        verificationCodeRepository.addVerificationCode(tempToken, verificationCode);
        javaMailSender.send(message);
    }

    @Override
    public void sendMailToFindPw(HttpServletRequest request, HttpServletResponse response, String email, String loginId) {

        String tempToken = jwtGenerator.generateTempToken(loginId);
        setHeader(tempToken, response);

        String verificationCode = createCode();
        MimeMessage message = createMessage(email, verificationCode);

        verificationCodeRepository.addVerificationCode(tempToken, verificationCode);
        javaMailSender.send(message);
    }

    private MimeMessage createMessage(String email, String verificationCode) {

        MimeMessage message = javaMailSender.createMimeMessage();

        try {
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setFrom(sender, senderName);
            helper.setTo(email);
            helper.setSubject(subject + verificationCode);

            String htmlContent = getHtmlContent();

            htmlContent = htmlContent.replace("${email}", email);
            htmlContent = htmlContent.replace("${verificationCode}", verificationCode);
            htmlContent = htmlContent.replace("${sender}", sender);

            helper.setText(htmlContent, true);

        } catch (MessagingException e) {
            throw new MailHandler(ErrorStatus.UNABLE_TO_SEND_EMAIL);
        } catch (IOException e) {
            throw new MailHandler(ErrorStatus.UNABLE_TO_LOAD_MAIL_FORM);
        } catch (Exception e) {
            throw new MailHandler(ErrorStatus.INTERNAL_SERVER_ERROR);
        }

        return message;
    }

    private String createCode() {
        Random random = new Random();
        int intCode = random.nextInt(1000000); // 1 ~ 999999 사이의 정수
        return String.format("%06d", intCode);
    }

    private String getHtmlContent() throws IOException {
        ClassPathResource resource = new ClassPathResource("templates/MailForm.html");
        byte[] encoded = Files.readAllBytes(Paths.get(resource.getURI()));
        return new String(encoded, StandardCharsets.UTF_8);
    }

    private void setHeader(String tempToken, HttpServletResponse response) {
        response.addHeader("Temp-Token", tempToken);
    }
}
