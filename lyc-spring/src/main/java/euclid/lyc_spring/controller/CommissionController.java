package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.request.CommissionRequestDTO;
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
    @GetMapping("/chats/commissions/{directorId}")
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
    // 의뢰가 승낙되기 전에 수정할 수도 있지않나.. 그래서 commissionId로 바꿔봄
    // 그래야 할 것 같긴 한데 근데 이거 수정을 어디서 함?
    // 수정이 안되는 부분이 있어서 코드를 수정함
    @PatchMapping("/chats/commissions/{commissionId}")
    public ApiResponse<CommissionDTO.CommissionViewDTO> updateCommission(
            @RequestBody CommissionRequestDTO.CommissionDTO commissionRequestDTO, @PathVariable Long commissionId) {
        CommissionDTO.CommissionViewDTO responseDTO = commissionCommandService.updateCommission(commissionId, commissionRequestDTO);
        return ApiResponse.onSuccess(SuccessStatus._COMMISSION_UPDATED, responseDTO);
    }

/*-------------------------------------------------- 저장한 옷 --------------------------------------------------*/

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "[구현중] 디렉터가 저장한 옷 목록 불러오기", description = """
            """)
    @GetMapping("/chats/{chatId}/commissions/saved-clothes")
    public void getAllCommissionedClothes(@PathVariable Long chatId) {}

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "[구현중] 디렉터가 저장한 옷 공유하기", description = """
            """)
    @PatchMapping("/chats/{chatId}/commissions/saved-clothes/public")
    public void changeCommissionedClothesPublic(@PathVariable Long chatId) {}

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "[구현중] 디렉터가 저장한 옷 공유 해제하기", description = """
            """)
    @PatchMapping("/chats/{chatId}/commissions/saved-clothes/private")
    public void changeCommissionedClothesPrivate(@PathVariable Long chatId) {}

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "[구현중] 옷 저장하기", description = """
            """)
    @PostMapping("/chats/{chatId}/commissions/saved-clothes")
    public void saveCommissionedClothes(@PathVariable Long chatId) {}

    @Tag(name = "Commission - Clothes", description = "저장한 옷 관련 API")
    @Operation(summary = "[구현중] 저장한 옷 삭제하기", description = """
            """)
    @DeleteMapping("/chats/{chatId}/commissions/saved-clothes/{clothesId}")
    public void deleteCommissionedClothes(
            @PathVariable Long chatId, @PathVariable Long clothesId) {}

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
