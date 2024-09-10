package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.request.MemberRequestDTO;
import euclid.lyc_spring.dto.request.VerificationRequestDTO;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.service.member.MemberCommandService;
import euclid.lyc_spring.service.member.MemberQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

/*-------------------------------------------------- 회원정보 설정 --------------------------------------------------*/

    @Operation(summary = "[구현완료] 회원 정보 불러오기", description = """
            회원 정보 변경화면의 회원 정보를 불러옵니다.
            """)
    @GetMapping("/members/info")
    public ApiResponse<MemberDTO.MemberSettingInfoDTO> getMemberInfo() {
        MemberDTO.MemberSettingInfoDTO responseDTO = memberQueryService.getMemberSettingInfo();
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_SETTING_INFO_FETCHED, responseDTO);
    }

    @Operation(summary = "[구현완료] 회원 정보 변경하기", description = """
            입력받은 회원 정보 데이터로 유저의 닉네임, 아이디, 자기소개, 프로필 이미지를 변경합니다.
            """)
    @PatchMapping("/members/info")
    public ApiResponse<MemberDTO.MemberSettingInfoDTO> updateMemberInfo(@RequestBody MemberRequestDTO.MemberSettingInfoDTO infoDTO) {
        MemberDTO.MemberSettingInfoDTO responseDTO = memberCommandService.updateMemberInfo(infoDTO);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_SETTING_INFO_UPDATED, responseDTO);
    }

    @Operation(summary = "[구현완료] 배송지 정보 불러오기", description = """
            유저의 배송지 정보를 불러옵니다.
            """)
    @GetMapping("/members/delivery")
    public ApiResponse<MemberDTO.AddressDTO> getDeliveryInfo() {
        MemberDTO.AddressDTO responseDTO = memberQueryService.getAdrress();
        return ApiResponse.onSuccess(SuccessStatus._ADDRESS_FETCHED, responseDTO);
    }

    @Operation(summary = "[구현완료] 배송지 정보 변경하기", description = """
            입력받은 배송지 데이터로 유저의 배송지 정보를 변경합니다.
            """)
    @PatchMapping("/members/delivery")
    public ApiResponse<MemberDTO.AddressDTO> updateDeliveryInfo(@RequestBody MemberRequestDTO.AddressReqDTO addressReqDTO) {
        MemberDTO.AddressDTO responseDTO = memberCommandService.updateAddress(addressReqDTO);
        return ApiResponse.onSuccess(SuccessStatus._ADDRESS_UPDATED, responseDTO);
    }

    // 보안에 신경쓰라고 하셨는데 이게 신경을 쓴건지 잘 모르겠습니다 함 봐주세용 ㅠ
    @Operation(summary = "[구현완료] 패스워드 변경하기", description = """
            기존 패스워드를 확인하고 일치하면 입력받은 패스워드로 변경합니다.
            """)
    @PatchMapping("/members/pw-info")
    public ApiResponse<MemberDTO.MemberPreviewDTO> updateLoginPw(HttpServletRequest request
            , @RequestBody VerificationRequestDTO.ChangePasswordDTO passwordDTO) {
        MemberDTO.MemberPreviewDTO responseDTO = memberCommandService.updateLoginPw(request, passwordDTO);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_PW_CHANGED, responseDTO);
    }

/*-------------------------------------------------- 푸시알림 설정 --------------------------------------------------*/

    @Operation(summary = "[구현완료] 기존 푸시알림 설정 불러오기", description = """
            푸시알림 설정 정보를 불러옵니다.
            """)
    @GetMapping("/members/push-sets")
    public ApiResponse<MemberDTO.PushSetDTO> getPushSet() {
        MemberDTO.PushSetDTO responseDTO = memberQueryService.getPushSet();
        return ApiResponse.onSuccess(SuccessStatus._PUSH_SET_FETCHED, responseDTO);
    }

    @Operation(summary = "[구현완료] 푸시알림 설정 변경하기", description = """
            입력받은 푸시알림 데이터로 유저의 푸시알림 정보를 변경합니다. 
            """)
    @PatchMapping("/members/push-sets")
    public ApiResponse<MemberDTO.PushSetDTO> updatePushSet(@RequestBody MemberRequestDTO.PushSetDTO pushSetDTO) {
        MemberDTO.PushSetDTO responseDTO = memberCommandService.updatePushSet(pushSetDTO);
        return ApiResponse.onSuccess(SuccessStatus._PUSH_SET_UPDATED, responseDTO);
    }

}
