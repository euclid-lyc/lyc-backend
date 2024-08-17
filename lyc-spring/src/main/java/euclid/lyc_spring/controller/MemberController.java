package euclid.lyc_spring.controller;

import euclid.lyc_spring.service.member.MemberCommandService;
import euclid.lyc_spring.service.member.MemberQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Member", description = "회원 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc/members")
public class MemberController {

    private final MemberQueryService memberQueryService;
    private final MemberCommandService memberCommandService;

/*-------------------------------------------------- 회원정보 설정 --------------------------------------------------*/

    @Operation(summary = "회원 정보 불러오기", description = """
            
            """)
    @GetMapping("/{memberId}/info")
    public void getMemberInfo(@PathVariable("memberId") Long memberId) {}

    @Operation(summary = "회원 정보 변경하기", description = """
            
            """)
    @PatchMapping("/{memberId}/info")
    public void updateMemberInfo(@PathVariable("memberId") Long memberId) {}

    @Operation(summary = "배송지 정보 불러오기", description = """
            
            """)
    @GetMapping("/{memberId}/delivery")
    public void getDeliveryInfo(@PathVariable("memberId") Long memberId) {}

    @Operation(summary = "배송지 정보 변경하기", description = """
            
            """)
    @PatchMapping("/{memberId}/delivery")
    public void updateDeliveryInfo(@PathVariable("memberId") Long memberId) {}

    @Operation(summary = "패스워드 변경하기", description = """
            
            """)
    @PatchMapping("/{memberId}/pw-info")
    public void updateLoginPw(@PathVariable("memberId") Long memberId) {}

/*-------------------------------------------------- 푸시알림 설정 --------------------------------------------------*/

    @Operation(summary = "기존 푸시알림 설정 불러오기", description = """
            
            """)
    @GetMapping("/{memberId}/push-sets")
    public void getPushSet(@PathVariable Long memberId) {}

    @Operation(summary = "푸시알림 설정 변경하기", description = """
            
            """)
    @GetMapping("/{memberId}/push-sets")
    public void updatePushSet(@PathVariable Long memberId) {}

}
