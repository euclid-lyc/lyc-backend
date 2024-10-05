package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.repository.MemberRepository;
import euclid.lyc_spring.service.admin.AdminService;
import euclid.lyc_spring.service.member.MemberCommandServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Admin", description = "관리자용 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc/admin")
public class AdminController {

    private final AdminService adminService;

/*-------------------------------------------------- Auth --------------------------------------------------*/

    @Operation(summary = "[구현완료] 회원 영구 삭제하기", description = """
            """)
    @DeleteMapping("/members/deletion")
    public ApiResponse<Void> deleteMembers() {
        adminService.deleteMembers();
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

    // 키워드 검색에서 인기도로 정렬하기 위해 사용되는 Member의 popularity룰 전부 초기화함
    // 주기적으로 리셋이 되어야 인기도가 고이지 않지않을까 한달이나 일주일 간격으로?
    @Operation(summary = "[구현완료] 인기도 초기화하기", description = """
            """)
    @PostMapping("/members/resetPopularity")
    public ApiResponse<Void> resetAllPopularity() {
        adminService.resetAllPopularity();
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }
/*-------------------------------------------------- Chat --------------------------------------------------*/

    @Operation(summary = "[구현완료] 채팅 영구 삭제하기", description = """
                """)
    @DeleteMapping("/chats/deletion")
    public ApiResponse<Void> deleteChats() {
        adminService.deleteChats();
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

/*-------------------------------------------------- Report --------------------------------------------------*/

    // 신고 처리하기 (누적 신고횟수가 5회 이상이면 계정 2주간 비활성화)

    // 처리된 신고 모두 삭제하기 (is_processed = true인 신고 삭제)


}
