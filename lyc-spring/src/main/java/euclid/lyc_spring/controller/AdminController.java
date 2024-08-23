package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.service.admin.AdminService;
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

    @Operation(summary = "회원 영구 삭제하기", description = """
            """)
    @DeleteMapping("/members/deletion")
    public ApiResponse<Void> deleteMember() {
        adminService.deleteMember();
        return ApiResponse.onSuccess(SuccessStatus._OK);
    }

}
