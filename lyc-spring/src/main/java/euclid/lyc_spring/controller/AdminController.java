package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.service.admin.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
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
