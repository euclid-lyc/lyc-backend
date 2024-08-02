package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.dto.request.RegisterDTO.*;
import euclid.lyc_spring.dto.response.MemberDTO.*;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Register", description = "회원가입 기능 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class RegisterController {

    private final RegisterService joinService;

    @Operation(summary = "회원가입하기", description = "회원을 생성합니다")
    @PostMapping("/register")
    public ApiResponse<MemberInfoDTO> register(@RequestBody RegisterMemberDTO registerMemberDTO){

        MemberInfoDTO memberInfoDTO = joinService.join(registerMemberDTO);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_CREATED, memberInfoDTO);
    }
}