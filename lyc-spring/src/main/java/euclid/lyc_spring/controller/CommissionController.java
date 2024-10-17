package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.request.CommissionRequestDTO;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import euclid.lyc_spring.dto.response.CommissionDTO;
import euclid.lyc_spring.service.commission.CommissionCommandService;
import euclid.lyc_spring.service.commission.CommissionQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc")
public class CommissionController {

    private final CommissionQueryService commissionQueryService;
    private final CommissionCommandService commissionCommandService;

    /*-------------------------------------------------- 의뢰서 --------------------------------------------------*/

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "[구현완료] 의뢰서 작성하기", description = """
            의뢰서 관련 데이터를 입력받아 commission 테이블에 새로운 의뢰를 생성합니다.
            """)
    @PostMapping("/chats/commissions")
    public ApiResponse<CommissionDTO.CommissionViewDTO> writeCommission(
            @RequestBody CommissionRequestDTO.CommissionDTO commissionRequestDTO) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.writeCommission(commissionRequestDTO);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_CREATED, responseDTO);
    }

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "[구현완료] 의뢰 목록(의뢰함) 불러오기", description = """
            로그인한 회원에게 요청된 의뢰의 목록이 반환됩니다.
            
            커서 기반 페이징이 적용됩니다. cursorDateTime은 이전에 전달된 마지막 의뢰의 업로드 시각입니다.
            
            이 API는 cursorDateTime보다 이전에 업로드된 의뢰의 목록을 불러옵니다.
            """)
    // 이거도 굳이 내 의뢰함만 보면 되니까 directerId 필요 없을 듯
    // 근데 의뢰함 기능 확인을 위해 아직 살려둠 나중에 수정하겠음~
    // 의뢰서 확인이랑 url이 겹쳐서 일단 all을 넣었는데 나중에 directerId 지우면서 지우겠음
    @GetMapping("/chats/commissions/all/{directorId}")
    public ApiResponse<List<CommissionDTO.CommissionViewDTO>> getAllCommissions(
            @PathVariable("directorId") Long directorId,
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam LocalDateTime cursorDateTime) {
        List<CommissionDTO.CommissionViewDTO> responseDTO = commissionQueryService.getAllCommissionList(directorId, pageSize, cursorDateTime);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_LIST_FETCHED, responseDTO);
    }

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "[구현완료] 의뢰서 확인하기", description = """
            의뢰서의 전체 내용을 확인합니다.
            """)
    @GetMapping("/chats/commissions/{commissionId}")
    public ApiResponse<CommissionDTO.CommissionInfoDTO> getCommission(@PathVariable Long commissionId) {
        CommissionDTO.CommissionInfoDTO responseDTO = commissionQueryService.getCommission(commissionId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_FETCHED, responseDTO);
    }

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "[구현완료] 의뢰 승낙하기", description = """
            의뢰서의 status룰 APPROVED로 변경합니다.
            
            의뢰를 승낙하면 자동으로 채팅방이 생성됩니다.
            """)
    @PatchMapping("/chats/commissions/{commissionId}/accept")
    public ApiResponse<CommissionDTO.CommissionViewDTO> acceptCommission(@PathVariable Long commissionId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.acceptCommission(commissionId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_ACCEPTED, responseDTO);
    }

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "[구현완료] 의뢰 거절하기", description = """
            의뢰서의 status를 TERMINATED로 변경합니다.
            """)
    @PatchMapping("/chats/commissions/{commissionId}/decline")
    public ApiResponse<CommissionDTO.CommissionViewDTO> declineCommission(@PathVariable Long commissionId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.declineCommission(commissionId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_DECLINED, responseDTO);
    }

    @Tag(name = "Commission - Request", description = "의뢰서 관련 API")
    @Operation(summary = "[구현완료] 의뢰서 수정하기", description = """
            의뢰서 관련 데이터를 입력받고 기존 의뢰서의 내용을 수정합니다.
            """)
    @PatchMapping("/chats/commissions/{commissionId}")
    public ApiResponse<CommissionDTO.CommissionViewDTO> updateCommission(
            @RequestBody CommissionRequestDTO.CommissionDTO commissionRequestDTO, @PathVariable Long commissionId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.updateCommission(commissionId, commissionRequestDTO);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_UPDATED, responseDTO);
    }

/*-------------------------------------------------- 저장한 옷 --------------------------------------------------*/

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "[구현완료] 디렉터가 저장한 옷 목록 불러오기", description = """
            저장된 옷의 imageUrl과 clothesUrl을 가져옵니다.
            """)
    @GetMapping("/chats/{chatId}/commissions/saved-clothes")
    public ApiResponse<List<CommissionDTO.ClothesViewDTO>> getAllCommissionedClothes(@PathVariable Long chatId) {
        List<CommissionDTO.ClothesViewDTO> responseDTO = commissionQueryService.getAllCommissionedClothes(chatId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_CLOTHES_LIST_FETCHED, responseDTO);
    }

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "[구현완료] 디렉터가 저장한 옷 공유하기", description = """
            디렉터가 저장한 옷을 의뢰자와 공유합니다.
            """)
    @PatchMapping("/chats/{chatId}/commissions/saved-clothes/public")
    public ApiResponse<ChatResponseDTO.ShareClothesListDTO> changeCommissionedClothesPublic(@PathVariable Long chatId) {
        ChatResponseDTO.ShareClothesListDTO responseDTO = commissionCommandService.changeCommissionedClothesPublic(chatId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_CLOTHES_PUBLIC, responseDTO);
    }

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "[구현완료] 디렉터가 저장한 옷 공유 해제하기", description = """
            디렉터가 저장한 옷을 더이상 의뢰자와 공유하지 않습니다.
            """)
    @PatchMapping("/chats/{chatId}/commissions/saved-clothes/private")
    public ApiResponse<ChatResponseDTO.ShareClothesListDTO> changeCommissionedClothesPrivate(@PathVariable Long chatId) {
        ChatResponseDTO.ShareClothesListDTO responseDTO = commissionCommandService.changeCommissionedClothesPrivate(chatId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_CLOTHES_PRIVATE, responseDTO);
    }

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "[구현완료] 옷 저장하기", description = """
            옷을 저장합니다.
            """)
    @PostMapping("/chats/{chatId}/commissions/saved-clothes")
    public ApiResponse<CommissionDTO.ClothesViewDTO> saveCommissionedClothes(@PathVariable Long chatId
            ,@RequestBody CommissionRequestDTO.ClothesDTO clotheRequestDTO) {
        CommissionDTO.ClothesViewDTO responseDTO = commissionCommandService.saveCommissionedClothes(chatId, clotheRequestDTO);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_CLOTHES_SAVED, responseDTO);
    }

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "[구현완료] 저장한 옷 삭제하기", description = """
            저장한 옷을 삭제합니다.
            """)
    @DeleteMapping("/chats/{chatId}/commissions/saved-clothes/{clothesId}")
    public ApiResponse<CommissionDTO.ClothesViewDTO> deleteCommissionedClothes(
            @PathVariable Long chatId, @PathVariable Long clothesId) {
        CommissionDTO.ClothesViewDTO responseDTO = commissionCommandService.deleteCommissionedClothes(chatId, clothesId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_CLOTHES_DELETED, responseDTO);
    }

/*-------------------------------------------------- 의뢰 종료 --------------------------------------------------*/

    @Tag(name = "Commission - Termination", description = "의뢰 종료 관련 API")
    @Operation(summary = "[구현완료] 의뢰 종료 요청하기", description = """
            의뢰서의 status를 WAIT_FOR_TERMINATION로 변경합니다.
            """)
    @PatchMapping("/chats/{chatId}/commissions/termination-request")
    public ApiResponse<CommissionDTO.CommissionViewDTO> requestCommissionTermination(@PathVariable Long chatId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.requestCommissionTermination(chatId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_REQUEST_TERMINATION, responseDTO);
    }

    @Tag(name = "Commission - Termination", description = "의뢰 종료 관련 API")
    @Operation(summary = "[구현완료] 의뢰 종료 승낙하기", description = """
            의뢰서의 status를 TERMINATED로 변경합니다.
            """)
    @PatchMapping("/chats/{chatId}/commissions/termination")
    public ApiResponse<CommissionDTO.CommissionViewDTO> terminateCommission(@PathVariable Long chatId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.terminateCommission(chatId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_TERMINATION, responseDTO);
    }

    @Tag(name = "Commission - Termination", description = "의뢰 종료 관련 API")
    @Operation(summary = "[구현완료] 의뢰 종료 거절하기", description = """
            의뢰서의 status룰 APPROVED로 변경합니다.
            """)
    @PatchMapping("/chats/{chatId}/commissions/termination-cancel")
    public ApiResponse<CommissionDTO.CommissionViewDTO> declineCommissionTermination(@PathVariable Long chatId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.declineCommissionTermination(chatId);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_TERMINATION, responseDTO);
    }

}
