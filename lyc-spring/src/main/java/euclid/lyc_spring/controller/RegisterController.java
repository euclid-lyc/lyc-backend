package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.dto.InfoDTO;
import euclid.lyc_spring.dto.InfoStyleListDTO;
import euclid.lyc_spring.dto.MemberDTO;
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
    public ApiResponse<Boolean> register(MemberDTO memberDTO, InfoDTO.BasicInfoDTO infodto, InfoStyleListDTO styleListDTO){

        Boolean result = joinService.join(memberDTO, infodto, styleListDTO);

        if(result)
            return ApiResponse.onSuccess(true);
        else
            return ApiResponse.onFailure("failed", "실패함", result);
    }

    @GetMapping("/")
    public String index(){
        return "index";
    }
}
