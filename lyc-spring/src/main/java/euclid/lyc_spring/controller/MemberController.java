package euclid.lyc_spring.controller;

import euclid.lyc_spring.service.MemberService;
import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.dto.TodayDirectorDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Member", description = "회원 기능 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/follower-order")
    ApiResponse<List<TodayDirectorDTO>> getTodayDirectorList() {
        List<TodayDirectorDTO> todayDirectorDTOList = memberService.getTodayDirectorList();
        return ApiResponse.onSuccess(todayDirectorDTOList);
    }
}
