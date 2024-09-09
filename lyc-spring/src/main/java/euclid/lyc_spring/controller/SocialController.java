package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.response.MemberDTO;
import euclid.lyc_spring.service.social.SocialCommandService;
import euclid.lyc_spring.service.social.SocialQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc/socials")
public class SocialController {

    private final SocialQueryService socialQueryService;
    private final SocialCommandService socialCommandService;

/*-------------------------------------------------- 회원 팔로우 및 팔로잉 --------------------------------------------------*/

    @Tag(name = "Social - Follow", description = "팔로우 & 팔로잉 관련 API")
    @Operation(summary = "[구현중] 팔로워 목록 불러오기", description = "팔로워 목록을 불러옵니다.")
    @GetMapping("/members/{memberId}/followers")
    public ApiResponse<List<MemberDTO.FollowDTO>> getFollowers(@PathVariable("memberId") Long memberId) {
        List<MemberDTO.FollowDTO> Followers = socialQueryService.getFollowerList(memberId);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_FOLLOWER_FOUND, Followers);
    }

    @Tag(name = "Social - Follow", description = "팔로우 & 팔로잉 관련 API")
    @Operation(summary = "[구현중] 팔로잉 목록 불러오기", description = "팔로잉 목록을 불러옵니다.")
    @GetMapping("/members/{memberId}/followings")
    public ApiResponse<List<MemberDTO.FollowDTO>> getFollowings(@PathVariable("memberId") Long memberId) {
        List<MemberDTO.FollowDTO> Followings = socialQueryService.getFollowingList(memberId);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_FOLLOWING_FOUND, Followings);
    }
    @Tag(name = "Social - Follow", description = "팔로우 & 팔로잉 관련 API")
    @Operation(summary = "[구현완료] 팔로우하기", description = "유저를 팔로우합니다.")
    @PostMapping("/members/{memberId}/followings")
    public ApiResponse<MemberDTO.MemberInfoDTO> followMember(@PathVariable("memberId") Long memberId) {
        MemberDTO.MemberInfoDTO memberInfoDTO = socialCommandService.followMember(memberId);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_FOLLOWED, memberInfoDTO);
    }

    @Tag(name = "Social - Follow", description = "팔로우 & 팔로잉 관련 API")
    @Operation(summary = "[구현완료] 언팔로우하기", description = "유저를 언팔로우합니다.")
    @DeleteMapping("/members/{memberId}/followings")
    public ApiResponse<MemberDTO.MemberInfoDTO>  unfollowMember(@PathVariable("memberId") Long memberId) {
        MemberDTO.MemberInfoDTO memberInfoDTO = socialCommandService.unfollowMember(memberId);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_FOLLOWING_DELETED, memberInfoDTO);
    }

/*-------------------------------------------------- 인기 디렉터 --------------------------------------------------*/

    @Tag(name = "Social - Director", description = "인기 회원(디렉터) 관련 API")
    @Operation(summary = "[구현중] 디렉터 목록 불러오기 (인기순)", description = """
            디렉터 찾기 화면에 표시할 디렉터 랭킹을 불러옵니다.
    
            커서 기반 페이징이 적용됩니다. (커서 : 팔로워 수, 커서가 null이면 오프셋이 0인 것과 동일)
            """)
    @GetMapping("/directors")
    public ApiResponse<MemberDTO.TodayDirectorListDTO> getPopularDirectors(
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam(required = false) Long followerCount
    ) {
        MemberDTO.TodayDirectorListDTO todayDirectorListDTO = socialQueryService.getPopularDirectors(pageSize, followerCount);
        return ApiResponse.onSuccess(SuccessStatus._TODAY_DIRECTOR_FETCHED, todayDirectorListDTO);
    }

/*-------------------------------------------------- 프로필 --------------------------------------------------*/

    @Tag(name = "Social - Member Info", description = "소셜 회원 정보 관련 API")
    @Operation(summary = "[구현완료] 프로필 불러오기", description = "회원의 기본 정보를 불러옵니다.")
    @GetMapping("/members/{memberId}")
    public ApiResponse<MemberDTO.MemberInfoDTO> getProfile(@PathVariable("memberId") Long memberId) {
        MemberDTO.MemberInfoDTO memberInfoDTO = socialQueryService.getMemberInfoDTO(memberId);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_FOUND, memberInfoDTO);
    }

    @Tag(name = "Social - Member Info", description = "소셜 회원 정보 관련 API")
    @Operation(summary = "[구현중] 스타일 정보 불러오기", description = """
    회원의 스타일 정보를 불러옵니다.
    
    비공개된 스타일 정보는 다른 회원이 열람할 수 없습니다.
    """)
    @GetMapping("/members/{memberId}/styles")
    void getStyleInfo(@PathVariable("memberId") Long memberId) {}

    @Tag(name = "Social - Member Info", description = "소셜 회원 정보 관련 API")
    @Operation(summary = "[구현중] 스타일 정보 변경하기", description = "로그인한 회원의 스타일 정보를 변경합니다.")
    @PatchMapping("/styles")
    void updateStyleInfo() {}

/*-------------------------------------------------- 회원 차단 --------------------------------------------------*/

    @Tag(name = "Social - Blocking", description = "회원 차단 관련 API")
    @Operation(summary = "[구현완료] 차단하기", description = "회원을 차단합니다.")
    @PostMapping("/block-members/{memberId}")
    ApiResponse<MemberDTO.MemberInfoDTO> blockMember(@RequestParam("myId") Long myId, @PathVariable("memberId") Long memberId) {
        MemberDTO.MemberInfoDTO memberInfoDTO = socialCommandService.blockMember(myId, memberId);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_BLOCKED, memberInfoDTO);
    }

    @Tag(name = "Social - Blocking", description = "회원 차단 관련 API")
    @Operation(summary = "[구현완료] 차단 해제하기", description = "회원의 차단을 해제합니다.")
    @DeleteMapping("/block-members/{memberId}")
    ApiResponse<MemberDTO.MemberInfoDTO> unblockMember(@RequestParam("myId") Long myId, @PathVariable("memberId") Long memberId) {
        MemberDTO.MemberInfoDTO memberInfoDTO = socialCommandService.unblockMember(myId, memberId);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_BLOCK_CANCELED, memberInfoDTO);
    }

    @Tag(name = "Social - Blocking", description = "회원 차단 관련 API")
    @Operation(summary = "[구현중] 차단 회원 목록 불러오기", description = "로그인한 회원이 차단한 회원의 목록을 불러옵니다.")
    @DeleteMapping("/block-members")
    void getAllBlockMembers() {}

/*-------------------------------------------------- 회원 신고 --------------------------------------------------*/

    @Tag(name = "Social - Report", description = "회원 신고 관련 API")
    @Operation(summary = "[구현중] 회원 신고하기", description = "로그인한 회원이 다른 회원을 신고합니다.")
    @PostMapping("/members/{memberId}/reports")
    void reportMember(@PathVariable("memberId") Long memberId) {}
}

