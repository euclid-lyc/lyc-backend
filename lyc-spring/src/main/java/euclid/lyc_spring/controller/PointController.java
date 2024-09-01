package euclid.lyc_spring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Point", description = "포인트 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc")
public class PointController {

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

    @Operation(summary = "[구현중] 보유 포인트 불러오기", description = """
            """)
    @GetMapping("/points")
    public void getPoint() {}

    @Operation(summary = "[구현중] 포인트 사용 내역 불러오기", description = """
            """)
    @GetMapping("/points/usages")
    public void getPointUsages() {}

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
