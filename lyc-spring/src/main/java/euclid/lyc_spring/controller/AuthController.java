package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
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
            
            """)
    @PatchMapping("/withdrawal")
    public void withdraw() {}

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
            """)
    @PostMapping("/sign-out")
    public ApiResponse<SignDTO.SignOutDTO> signOut(HttpServletRequest request) {
        SignDTO.SignOutDTO signOutResponseDTO = authCommandService.signOut(request);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_SIGNED_OUT, signOutResponseDTO);
    }

    @Operation(summary = "아이디 찾기 - 가입정보 확인하기", description = """
            
            """)
    @PostMapping("/find-id/infos")
    public void checkInfoToFindId() {}

    @Operation(summary = "아이디 찾기", description = """
            
            """)
    @GetMapping("/find-id")
    public void findId() {}

    @Operation(summary = "비밀번호 찾기 - 가입정보 확인하기", description = """
            
            """)
    @PostMapping("/find-pw/infos")
    public void checkInfoToFindPw() {}

    @Operation(summary = "비밀번호 찾기", description = """
            
            """)
    @PostMapping("/find-pw")
    public void findPw() {}

}
