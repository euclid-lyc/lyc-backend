package euclid.lyc_spring.controller;

import euclid.lyc_spring.service.MemberService;
import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.dto.response.MemberDTO.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Member", description = "회원 기능 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {

    private final MemberService memberService;

    @Operation(summary = "오늘의 디렉터 불러오기", description = "홈화면에 오늘의 디렉터 10명을 불러옵니다.")
    @GetMapping("/members/follower-order")
    ApiResponse<List<TodayDirectorDTO>> getTodayDirectorList() {
        List<TodayDirectorDTO> todayDirectorDTOList = memberService.getTodayDirectorList();
        return ApiResponse.onSuccess(todayDirectorDTOList);
    }

    @Operation(summary = "유저 정보 불러오기", description = "유저의 정보를 불러옵니다.")
    @GetMapping("/api/members/{memberId}")
    ApiResponse<MemberInfoDTO> getMember(@PathVariable("memberId") long memberId) {
        MemberInfoDTO memberInfoDTO = memberService.getMemberInfoDTO(memberId);
        return ApiResponse.onSuccess(memberInfoDTO);
    }

    @Operation(summary = "팔로워 목록 불러오기", description = "팔로워 목록을 불러옵니다.")
    @GetMapping("/api/members/{memberId}/followers")
    ApiResponse<List<FollowDTO>> getFollowers(@PathVariable("memberId") long memberId) {
        List<FollowDTO> Followers = memberService.getFollowerList(memberId);
        return ApiResponse.onSuccess(Followers);
    }

    @Operation(summary = "팔로잉 목록 불러오기", description = "팔로잉 목록을 불러옵니다.")
    @GetMapping("/api/members/{memberId}/followings")
    ApiResponse<List<FollowDTO>> getFollowings(@PathVariable("memberId") long memberId) {
        List<FollowDTO> Followings = memberService.getFollowingList(memberId);
        return ApiResponse.onSuccess(Followings);
    }

    @Operation(summary = "팔로우하기", description = "유저를 팔로우합니다.")
    @PostMapping("/api/followings/{memberId}?id={myId}")
    ApiResponse<String>  followMember(@PathVariable("myId") Long myId, @PathVariable("memberId") Long memberId) {
        memberService.followMember(myId, memberId);
        return ApiResponse.onSuccess("팔로우 되었습니다");
    }

    @Operation(summary = "팔로잉 삭제하기", description = "유저를 언팔로우합니다.")
    @DeleteMapping("/api/follower/{memberId}?id={myId}")
    ApiResponse<String>  unfollowMember(@PathVariable("myId") Long myId, @PathVariable("memberId") Long memberId) {
        memberService.unfollowMember(myId, memberId);
        return ApiResponse.onSuccess("팔로우 삭제되었습니다.");
    }

    @Operation(summary = "차단하기", description = "해당 유저를 차단합니다.")
    @PostMapping("/api/block-members/{memberId}?id={myId}")
    ApiResponse<String> blockMember(@PathVariable("myId") Long myId, @PathVariable("memberId") Long memberId) {
        memberService.blockMember(myId, memberId);
        return ApiResponse.onSuccess("유저를 차단했습니다.");
    }

    @Operation(summary = "차단 해제하기", description = "해당 유저의 차단을 해제합니다.")
    @DeleteMapping("/api/block-members/{memberId}?id={myId}")
    ApiResponse<String> unblockMember(@PathVariable("myId") Long myId, @PathVariable("memberId") Long memberId) {
        memberService.unblockMember(myId, memberId);
        return ApiResponse.onSuccess("유저 차단을 해제했습니다.");
    }
}