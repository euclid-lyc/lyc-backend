package euclid.lyc_spring.controller;

import euclid.lyc_spring.service.commission.CommissionCommandService;
import euclid.lyc_spring.service.commission.CommissionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc")
public class CommissionController {

    private final CommissionQueryService commissionQueryService;
    private final CommissionCommandService commissionCommandService;

/*-------------------------------------------------- 의뢰서 --------------------------------------------------*/

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "의뢰서 작성하기", description = """
            """)
    @PostMapping("/chats/commissions")
    public void writeCommission() {}

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "의뢰 목록(의뢰함) 불러오기", description = """
            """)
    @GetMapping("/chats/commissions")
    public void getAllCommissions() {}

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "의뢰서 확인하기", description = """
            """)
    @GetMapping("/chats/commissions/{commissionId}")
    public void getCommission(@PathVariable Long commissionId) {}

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "의뢰 승낙하기", description = """
            """)
    @PatchMapping("/chats/commissions/{commissionId}/accept")
    public void acceptCommission(@PathVariable Long commissionId) {}

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "의뢰 거절하기", description = """
            """)
    @PatchMapping("/chats/commissions/{commissionId}/decline")
    public void declineCommission(@PathVariable Long commissionId) {}

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "의뢰서 수정하기", description = """
            """)
    @PatchMapping("/chats/{chatId}/commissions")
    public void updateCommission() {}

/*-------------------------------------------------- 저장한 옷 --------------------------------------------------*/

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "디렉터가 저장한 옷 목록 불러오기", description = """
            """)
    @GetMapping("/chats/{chatId}/commissions/saved-clothes")
    public void getAllCommissionedClothes(@PathVariable Long chatId) {}

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "디렉터가 저장한 옷 공유하기", description = """
            """)
    @PatchMapping("/chats/{chatId}/commissions/saved-clothes/public")
    public void changeCommissionedClothesPublic(@PathVariable Long chatId) {}

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "디렉터가 저장한 옷 공유 해제하기", description = """
            """)
    @PatchMapping("/chats/{chatId}/commissions/saved-clothes/private")
    public void changeCommissionedClothesPrivate(@PathVariable Long chatId) {}

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "옷 저장하기", description = """
            """)
    @PostMapping("/chats/{chatId}/commissions/saved-clothes")
    public void saveCommissionedClothes(@PathVariable Long chatId) {}

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "저장한 옷 삭제하기", description = """
            """)
    @DeleteMapping("/chats/{chatId}/commissions/saved-clothes/{clothesId}")
    public void deleteCommissionedClothes(
            @PathVariable Long chatId, @PathVariable Long clothesId) {}

/*-------------------------------------------------- 의뢰 종료 --------------------------------------------------*/

    @Tag(name = "Commission - Termination", description = "의뢰 종료 관련 API")
    @Operation(summary = "의뢰 종료 요청하기", description = """
            """)
    @PatchMapping("/chats/{chatId}/commissions/termination-request")
    public void requestCommissionTermination(@PathVariable Long chatId) {}

    @Tag(name = "Commission - Termination", description = "의뢰 종료 관련 API")
    @Operation(summary = "의뢰 종료 승낙하기", description = """
            """)
    @PatchMapping("/chats/{chatId}/commissions/termination")
    public void terminateCommission(@PathVariable Long chatId) {}

    @Tag(name = "Commission - Termination", description = "의뢰 종료 관련 API")
    @Operation(summary = "의뢰 종료 거절하기", description = """
            """)
    @PatchMapping("/chats/{chatId}/commissions/termination-cancel")
    public void declineComissionTermination(@PathVariable Long chatId) {}

}
