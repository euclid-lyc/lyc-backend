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
    public ApiResponse<MemberDTO.MemberInfoDTO> register(@RequestBody RegisterDTO.RegisterMemberDTO registerMemberDTO){

        MemberDTO.MemberInfoDTO memberInfoDTO = authCommandService.join(registerMemberDTO);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_CREATED, memberInfoDTO);
    }

    @Operation(summary = "회원 탈퇴하기", description = """
            
            """)
    @PatchMapping("/withdrawal")
    public void withdraw() {}

/*-------------------------------------------------- 로그인 및 로그아웃 --------------------------------------------------*/

    @Operation(summary = "로그인 하기", description = """
            
            """)
    @PostMapping("/sign-in")
    public ApiResponse<SignDTO.SignInDTO> signIn(@RequestBody SignRequestDTO.SignInDTO signInRequestDTO, HttpServletResponse response) {
        SignDTO.SignInDTO signInResponseDTO = authCommandService.signIn(signInRequestDTO, response);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_SIGNED_IN, signInResponseDTO);
    }

    @Operation(summary = "로그아웃 하기", description = """
            
            """)
    @PostMapping("/sign-out")
    public void signOut() {}

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
