package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.request.*;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.dto.response.SignDTO;
import euclid.lyc_spring.service.auth.AuthCommandService;
import euclid.lyc_spring.service.auth.AuthQueryService;
import euclid.lyc_spring.service.mail.MailService;
import euclid.lyc_spring.service.s3.S3ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc/auths")
public class AuthController {

    private final AuthQueryService authQueryService;
    private final AuthCommandService authCommandService;
    private final S3ImageService s3ImageService;
    private final MailService mailService;

/*-------------------------------------------------- 회원가입 및 탈퇴 --------------------------------------------------*/

    @Operation(summary = "회원가입 하기", description = """
    
            """)
    @PostMapping(value = "/sign-up", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<MemberDTO.MemberInfoDTO> signUp(
            @RequestPart RegisterDTO.RegisterMemberDTO registerMemberDTO,
            @RequestPart(required = false) MultipartFile image) {
        String imageUrl = s3ImageService.upload(image);
        MemberDTO.MemberInfoDTO responseDTO = authCommandService.join(registerMemberDTO, imageUrl);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_CREATED, responseDTO);
    }

    @Operation(summary = "회원 탈퇴하기", description = """
            요청을 보낸 시점을 기준으로 회원을 비활성화합니다(inactive 값을 지정합니다).
            
            회원 정보는 관리자 API(deleteMember)에 의해 30일 뒤에 DB에서 자동으로 삭제됩니다.
            """)
    @PatchMapping("/withdrawal")
    public ApiResponse<MemberDTO.MemberPreviewDTO> withdraw() {
        MemberDTO.MemberPreviewDTO withdrawnMemberDTO = authCommandService.withdraw();
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_WITHDRAWN, withdrawnMemberDTO);
    }

/*-------------------------------------------------- 로그인 및 로그아웃 --------------------------------------------------*/

    @Operation(summary = "로그인 하기", description = """
            회원 id와 비밀번호르 입력 받아 일치 여부를 확인한 후, 회원 정보가 일치하는 경우 토큰을 반환합니다.
            
            access token은 회원이 API를 사용하는 데 필요한 인증 정보입니다. 클라이언트에서 별도로 관리 바랍니다.
            """)
    @PostMapping("/sign-in")
    public ApiResponse<SignDTO.SignInDTO> signIn(@RequestBody SignRequestDTO.SignInDTO signInRequestDTO, HttpServletResponse response) {
        SignDTO.SignInDTO signInResponseDTO = authCommandService.signIn(signInRequestDTO, response);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_SIGNED_IN, signInResponseDTO);
    }

    @Operation(summary = "로그아웃 하기", description = """
            로그인한 회원의 인증을 해제하고 Refresh Token을 삭제합니다.
            
            기존의 accessToken은 BlackList에 등록되며, 만료기간이 지나면 자동으로 삭제됩니다.
            """)
    @PostMapping("/sign-out")
    public ApiResponse<SignDTO.SignOutDTO> signOut(HttpServletRequest request) {
        SignDTO.SignOutDTO signOutResponseDTO = authCommandService.signOut(request);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_SIGNED_OUT, signOutResponseDTO);
    }

    @Operation(summary = "아이디 찾기 - 이메일 인증 코드 전송", description = """
            가입된 이메일로 인증 코드를 전송합니다.
            """)
    @PostMapping("/sign-in/find-id/send-verification-code")
    public ApiResponse<Void> sendVerificationCodeToFindId(
            HttpServletRequest request, HttpServletResponse response,
            @RequestBody VerificationRequestDTO.FindIdDTO findIdDTO) {
        if (findIdDTO.getEmail() != null && !findIdDTO.getEmail().isEmpty()) {
            mailService.checkEmail(findIdDTO);
            mailService.sendMailToFindId(request, response, findIdDTO.getEmail());
            return ApiResponse.onSuccess(SuccessStatus._VERIFICATION_CODE_SENT);
        } else {
            return ApiResponse.onFailure(ErrorStatus.UNABLE_TO_SEND_VERIFICATION_CODE);
        }
    }

    @Operation(summary = "아이디 찾기 - 인증 코드 검증", description = """
            이름과 이메일(혹은 전화번호)를 RequestBody로 받아 회원의 아이디를 반환합니다.
            
            해당 API는 클라이언트에서 이메일 인증을 먼저 수행한 후 송신 가능합니다.
            
            Request Header에는 반드시 임시 토큰이 포함되어 있어야 합니다.
            """)
    @PostMapping("/find-id")
    public ApiResponse<MemberDTO.MemberPreviewDTO> findId(
            HttpServletRequest request, @RequestBody VerificationRequestDTO.IdVerificationDTO idVerificationDTO) {
        MemberDTO.MemberPreviewDTO memberPreviewDTO = authCommandService.findId(request, idVerificationDTO);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_LOGIN_ID_FOUND, memberPreviewDTO);
    }

    @Operation(summary = "비밀번호 찾기 - 이메일 인증 코드 전송", description = """
            가입된 이메일로 인증 코드를 전송합니다.
            """)
    @PostMapping("/sign-in/find-pw/send-verification-code")
    public ApiResponse<Void> sendVerificationCodeToFindPw(
            HttpServletRequest request, HttpServletResponse response,
            @RequestBody VerificationRequestDTO.FindPwDTO findPwDTO) {
        mailService.checkEmail(findPwDTO);
        mailService.sendMailToFindPw(request, response, findPwDTO.getEmail(), findPwDTO.getLoginId());
        return ApiResponse.onSuccess(SuccessStatus._VERIFICATION_CODE_SENT);
    }

    @Operation(summary = "비밀번호 찾기 - 인증 코드 검증", description = """
            인증 코드 및 회원 정보를 확인합니다.
            
            해당 API는 클라이언트에서 이메일 인증을 먼저 수행한 후 송신 가능합니다.
            
            Request Header에는 반드시 임시 토큰이 포함되어 있어야 합니다.
         
            """)
    @PostMapping("/find-pw")
    public ApiResponse<Void> findPw(
            HttpServletRequest request, @RequestParam String code) {
        authCommandService.findPw(request, code);
        return ApiResponse.onSuccess(SuccessStatus._VERIFICATION_CODE_CHECKED);
    }

    @Operation(summary = "비밀번호 찾기 - 비밀번호 변경", description = """
            비밀번호와 비밀번호 확인을 RequestBody로 받아 비밀번호르 변경합니다.
            """)
    @PatchMapping("/find-pw/update")
    public ApiResponse<MemberDTO.MemberPreviewDTO> updatePw(
            HttpServletRequest request, @RequestBody VerificationRequestDTO.PwVerificationDTO pwVerificationDTO) {
        MemberDTO.MemberPreviewDTO memberPreviewDTO = authCommandService.updatePw(request, pwVerificationDTO);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_PW_CHANGED, memberPreviewDTO);
    }

}
