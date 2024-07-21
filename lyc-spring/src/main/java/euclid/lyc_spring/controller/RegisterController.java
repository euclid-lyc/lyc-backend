package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.dto.request.RegisterDTO.*;
import euclid.lyc_spring.dto.response.MemberDTO.*;
import euclid.lyc_spring.service.RegisterService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@AllArgsConstructor
@Tag(name = "register", description = "회원가입 기능 관련 API")
public class RegisterController {

    private final RegisterService joinService;

    @Operation(summary = "회원가입하기", description = "회원을 생성합니다")
    @PostMapping("/api/register")
    public ApiResponse<MemberInfoDTO> register(@RequestBody RegisterMemberDTO registerMemberDTO){

        MemberInfoDTO memberInfoDTO = joinService.join(registerMemberDTO);
        return ApiResponse.onSuccess(memberInfoDTO);
    }

    @GetMapping("/")
    public String index(){
        System.out.println(123);
        return "index";
    }
}