package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.request.MemberRequestDTO;
import euclid.lyc_spring.dto.request.RegisterDTO;
import euclid.lyc_spring.dto.request.SignRequestDTO;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.dto.response.SignDTO;
import euclid.lyc_spring.service.auth.AuthCommandService;
import euclid.lyc_spring.service.auth.AuthQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc/auths")
public class AuthController {

    private final AuthQueryService authQueryService;
    private final AuthCommandService authCommandService;

/*-------------------------------------------------- 회원가입 및 탈퇴 --------------------------------------------------*/

    @Operation(summary = "회원가입 하기", description = """
    
            """)
    @PostMapping("/sign-up")
    public ApiResponse<MemberDTO.MemberInfoDTO> signUp(
            @RequestBody RegisterDTO.RegisterMemberDTO registerMemberDTO) {
        MemberDTO.MemberInfoDTO responseDTO = authCommandService.join(registerMemberDTO);
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

    @Operation(summary = "아이디 찾기", description = """
            이름과 이메일(혹은 전화번호)를 RequestBody로 받아 회원의 아이디를 반환합니다.
            
            RequestBody의 "method"에는 ["EMAIL", "PHONE"] 중 하나가 입력되어야 합니다.
            
            해당 API는 반드시 클라이언트에서 이메일 인증(혹은 전화번호 인증)을 마친 후 송신 바랍니다.
            
            임시 토큰 발급을 해야 할 것 같은데 어떻게 해야 될지 모르겠어서 일단 패스함
            """)
    @PostMapping("/sign-in/find-id")
    public ApiResponse<MemberDTO.MemberPreviewDTO> findId(
            @RequestBody MemberRequestDTO.MemberAuthDTO memberAuthDTO) {
        MemberDTO.MemberPreviewDTO memberPreviewDTO = authCommandService.findId(memberAuthDTO);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_LOGIN_ID_FOUND, memberPreviewDTO);
    }

    @Operation(summary = "비밀번호 찾기 - 가입정보 확인하기", description = """
            이름과 아이디, 이메일(혹은 전화번호)를 RequestBody로 받아 가입 정보를 확인합니다.
            
            RequestBody의 "method"에는 ["EMAIL", "PHONE"] 중 하나가 입력되어야 합니다.
            
            해당 API는 반드시 클라이언트에서 이메일 인증(혹은 전화번호 인증)을 마친 후 송신 바랍니다.
            
            임시 토큰 발급을 해야 할 것 같은데 어떻게 해야 될지 모르겠어서 일단 패스함
            """)
    @PostMapping("/sign-in/find-pw/infos")
    public ApiResponse<MemberDTO.MemberPreviewDTO> checkInfoToFindPw(
            @RequestBody MemberRequestDTO.MemberPwAuthDTO memberPwAuthDTO) {
        MemberDTO.MemberPreviewDTO memberPreviewDTO = authCommandService.checkInfoToFindPw(memberPwAuthDTO);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_FOUND, memberPreviewDTO);
    }

    @Operation(summary = "비밀번호 찾기 - 비밀번호 변경", description = """
            비밀번호와 비밀번호 확인을 RequestBody로 받아 비밀번호를 변경합니다.
            
            Request Header에는 반드시 임시 토큰이 포함되어 있어야 합니다.
            
            그래서 임시 토큰 발급을 해야 할 것 같은데 어떻게 해야 될지 모르겠어서 일단 패스함
            """)
    @PatchMapping("sign-in/find-pw")
    public ApiResponse<MemberDTO.MemberPreviewDTO> findPw(
            @RequestBody SignRequestDTO.PasswordDTO passwordDTO) {
        MemberDTO.MemberPreviewDTO memberPreviewDTO = authCommandService.findPw(passwordDTO);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_PW_CHANGED, memberPreviewDTO);
    }

}
