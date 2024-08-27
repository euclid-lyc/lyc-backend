package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.request.CommissionRequestDTO;
import euclid.lyc_spring.dto.response.CommissionDTO;
import euclid.lyc_spring.service.commission.CommissionCommandService;
import euclid.lyc_spring.service.commission.CommissionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public ApiResponse<CommissionDTO.CommissionViewDTO> writeCommission(
            @RequestBody CommissionRequestDTO.CommissionDTO commissionRequestDTO) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.writeCommission(commissionRequestDTO);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_CREATED, responseDTO);
    }

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "의뢰 목록(의뢰함) 불러오기", description = """
            """)
    @GetMapping("/chats/commissions/{directorId}")
    public ApiResponse<List<CommissionDTO.CommissionViewDTO>> getAllCommissions(@PathVariable("directorId") Long directorId) {
        List<CommissionDTO.CommissionViewDTO> responseDTO = commissionQueryService.getAllCommissionList(directorId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_LIST_FETCHED, responseDTO);
    }

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "의뢰서 확인하기", description = """
            """)
    @GetMapping("/chats/commissions/commission/{commissionId}")
    public ApiResponse<CommissionDTO.CommissionInfoDTO> getCommission(@PathVariable Long commissionId) {
        CommissionDTO.CommissionInfoDTO responseDTO = commissionQueryService.getCommission(commissionId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_FETCHED, responseDTO);
    }

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "의뢰 승낙하기", description = """
            """)
    @PatchMapping("/chats/commissions/{commissionId}/accept")
    public ApiResponse<CommissionDTO.CommissionViewDTO> acceptCommission(@PathVariable Long commissionId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.acceptCommission(commissionId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_ACCEPTED, responseDTO);
    }

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "의뢰 거절하기", description = """
            """)
    @PatchMapping("/chats/commissions/{commissionId}/decline")
    public ApiResponse<CommissionDTO.CommissionViewDTO> declineCommission(@PathVariable Long commissionId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.declineCommission(commissionId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_DECLINED, responseDTO);
    }

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "의뢰서 수정하기", description = """
            """)
    // 의뢰가 승낙되기 전에 수정할 수도 있지않나.. 그래서 commissionId로 바꿔봄
    @PatchMapping("/chats/{commissionId}/commissions")
    public ApiResponse<CommissionDTO.CommissionViewDTO> updateCommission(
            @RequestBody CommissionRequestDTO.CommissionDTO commissionRequestDTO, @PathVariable Long commissionId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.updateCommission(commissionId, commissionRequestDTO);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_UPDATED, responseDTO);
    }

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
    public ApiResponse<CommissionDTO.CommissionViewDTO> requestCommissionTermination(@PathVariable Long chatId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.requestCommissionTermination(chatId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_REQUEST_TERMINATION, responseDTO);
    }

    @Tag(name = "Commission - Termination", description = "의뢰 종료 관련 API")
    @Operation(summary = "의뢰 종료 승낙하기", description = """
            """)
    @PatchMapping("/chats/{chatId}/commissions/termination")
    public ApiResponse<CommissionDTO.CommissionViewDTO> terminateCommission(@PathVariable Long chatId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.terminateCommission(chatId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_TERMINATION, responseDTO);
    }

    @Tag(name = "Commission - Termination", description = "의뢰 종료 관련 API")
    @Operation(summary = "의뢰 종료 거절하기", description = """
            """)
    @PatchMapping("/chats/{chatId}/commissions/termination-cancel")
    public ApiResponse<CommissionDTO.CommissionViewDTO> declineCommissionTermination(@PathVariable Long chatId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.declineCommissionTermination(chatId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_TERMINATION, responseDTO);
    }

}
