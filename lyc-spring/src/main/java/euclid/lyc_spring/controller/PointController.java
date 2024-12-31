package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.response.PointResDTO;
import euclid.lyc_spring.service.point.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Point", description = "포인트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc")
public class PointController {

    private final PointService pointService;

/*-------------------------------------------------- 출석체크 포인트 --------------------------------------------------*/

    @Operation(summary = "[구현중] 출석 체크하기", description = """
            """)
    @PostMapping("/attendances")
    public void attendant(
            @RequestParam Integer year,
            @RequestParam Integer month,
            @RequestParam Integer day) {}

    @Operation(summary = "[구현중] 출석 보상 받기", description = """
            """)
    @PatchMapping("/attendances/rewards")
    public void getAttendanceReward() {}

/*-------------------------------------------------- 스탬프 포인트 --------------------------------------------------*/

    @Operation(summary = "[구현중] 스탬프 개수 불러오기", description = """
            """)
    @GetMapping("/stamps")
    public void openStampBoard() {}

    @Operation(summary = "[구현중] 스탬프 발급 받기", description = """
            스탬프의 개수를 1만큼 추가합니다.
            
            만약 스탬프의 개수가 10개인 경우 2000포인트를 지급하고 스탬프 개수를 초기화합니다.
            """)
    @PatchMapping("/stamps/chats/{chatId}")
    public void issueStamp(@PathVariable Long chatId) {}

/*-------------------------------------------------- 포인트 거래 --------------------------------------------------*/

    @Operation(summary = "[구현중] 보유 포인트 조회하기", description = """
            로그인한 회원이 보유중인 포인트를 조회합니다.
            """)
    @GetMapping("/points")
    public ApiResponse<PointResDTO.MemberPointDTO> getMyPoints() {
        PointResDTO.MemberPointDTO memberPointDTO = pointService.getMyPoints();
        return ApiResponse.onSuccess(SuccessStatus.POINT_FOUND, memberPointDTO);
    }

    @Operation(summary = "[구현중] 포인트 사용 내역 불러오기", description = """
            로그인한 회원의 포인트 사용 내역을 최신순으로 조회합니다.
            * pageSize : 요청할 페이지 사이즈
            * cursorCreatedAt : 이전 응답의 마지막 usedAt
            """)
    @GetMapping("/points/usages")
    public ApiResponse<PointResDTO.UsageListDTO> getMyPointUsages(
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam(required = false) LocalDateTime cursorUsedAt
        ) {
        PointResDTO.UsageListDTO usageListDTO = pointService.getMyPointUsages(pageSize, cursorUsedAt);
        return ApiResponse.onSuccess(SuccessStatus.POINT_USAGES_FOUND, usageListDTO);
    }

    @Operation(summary = "[구현중] 카카오페이로 포인트 충전하기", description = """
            """)
    @PatchMapping("/points/recharge/kakao")
    public void rechargePointByKakao() {}

    @Operation(summary = "[구현중] 네이버페이로 포인트 충전하기", description = """
            """)
    @PatchMapping("/points/recharge/naver")
    public void rechargePointByNaver() {}

    @Operation(summary = "[구현중] 내 계좌로 포인트 이체하기", description = """
            """)
    @PatchMapping("/points/transfer")
    public void transferPointToAccount() {}
}
