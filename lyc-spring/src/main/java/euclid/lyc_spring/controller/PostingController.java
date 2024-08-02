package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.dto.request.PostingRequestDTO.*;
import euclid.lyc_spring.dto.response.PostingDTO.*;
import euclid.lyc_spring.service.PostingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Post", description = "게시글 기능 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostingController {

    private final PostingService postingService;

    /**
     * GET API
     */

    @Operation(summary = "유저의 코디 목록 불러오기", description = "마이페이지에 유저의 코디 목록을 불러옵니다.")
    @GetMapping("/members/{memberId}/coordies")
    ApiResponse<PostingImageListDTO> getAllMemberCoordies(@PathVariable("memberId") Long memberId) {
        PostingImageListDTO postingImageListDTO = postingService.getAllMemberCoordies(memberId);
        return ApiResponse.onSuccess(postingImageListDTO);
    }

    @Operation(summary = "유저의 리뷰 목록 불러오기", description = "마이페이지에 유저의 리뷰 목록을 불러옵니다.")
    @GetMapping("/members/{memberId}/reviews")
    ApiResponse<PostingImageListDTO> getAllMemberReviews(@PathVariable("memberId") Long memberId) {
        PostingImageListDTO postingImageListDTO = postingService.getAllMemberReviews(memberId);
        return ApiResponse.onSuccess(postingImageListDTO);
    }

    @Operation(summary = "저장한 코디 목록 불러오기", description = "마이페이지에 저장한 코디 목록을 불러옵니다.")
    @GetMapping("/members/{memberId}/saved-postings")
    ApiResponse<PostingImageListDTO> getAllSavedCoordies(@PathVariable("memberId") Long memberId) {
        PostingImageListDTO postingImageListDTO = postingService.getAllSavedCoordies(memberId);
        return ApiResponse.onSuccess(postingImageListDTO);
    }

    @Operation(summary = "저장한 게시글(코디 or 리뷰) 불러오기", description = "게시글을 불러옵니다.")
    @GetMapping("/members/{memberId}/postings/{postingId}/saved-postings/{savedPostingId}")
    ApiResponse<PostingViewDTO> getSavedCoordie(@PathVariable("memberId") Long memberId,
                                                @PathVariable("postingId") Long postingId,
                                                @PathVariable("savedPostingId") Long savedPostingId) {
        PostingViewDTO postingViewDTO = postingService.getSavedCoordie(memberId, postingId, savedPostingId);
        return ApiResponse.onSuccess(postingViewDTO);
    }

    @Operation(summary = "게시글 좋아요 클릭 여부 불러오기", description = "게시글에 좋아요를 눌렀는지 확인합니다.")
    @GetMapping("/members/{memberId}/postings/{postingId}/likes")
    ApiResponse<ClickDTO> getIsClickedLike(@PathVariable("memberId") Long memberId,
                                           @PathVariable("postingId") Long postingId) {
        ClickDTO clickDTO = postingService.getIsClickedLike(memberId, postingId);
        return ApiResponse.onSuccess(clickDTO);
    }

    @Operation(summary = "게시글 저장 여부 불러오기", description = "게시글을 저장하였는지 확인합니다.")
    @GetMapping("/members/{memberId}/postings/{postingId}/saves")
    ApiResponse<ClickDTO> getIsClickedSave(@PathVariable("memberId") Long memberId,
                                           @PathVariable("postingId") Long postingId) {
        ClickDTO clickDTO = postingService.getIsClickedSave(memberId, postingId);
        return ApiResponse.onSuccess(clickDTO);
    }

    /**
     * POST API
     */

    @Operation(summary = "게시글(코디 or 리뷰) 작성하기", description = "게시글을 작성합니다.")
    @PostMapping("/members/{memberId}/postings")
    ApiResponse<PostingViewDTO> createPosting(@PathVariable("memberId") Long memberId,
                                              @RequestBody PostingSaveDTO postingSaveDTO) {
        PostingViewDTO postingViewDTO = postingService.createPosting(memberId, postingSaveDTO);
        return ApiResponse.onSuccess(postingViewDTO);
    }

    @Operation(summary = "좋아요 누르기", description = "게시글에 좋아요를 누릅니다.")
    @PostMapping("/api/postings/{postingId}/like?id={myId}")
    public ApiResponse<String> likePosting(@PathVariable("postingId") Long postingId, @PathVariable("myId") Long myId) {
        postingService.likePosting(myId, postingId);
        return ApiResponse.onSuccess("게시글에 좋아요를 추가했습니다.");
    }

    @Operation(summary = "코디 저장하기", description = "게시글을 저장합니다")
    @PostMapping("/api/postings/{postingId}?id={myId}")
    public ApiResponse<String> savedPosting(@PathVariable("myId") Long myId, @PathVariable("postingId") Long postingId) {
        postingService.savedPosting(myId, postingId);
        return ApiResponse.onSuccess("게시글을 저장하였습니다.");
    }

    /**
     * DELETE API
     */

    @Operation(summary = "게시글(코디 or 리뷰) 삭제하기", description = "게시글을 삭제합니다.")
    @DeleteMapping("/members/{memberId}/postings/{postingId}")
    ApiResponse<Long> deletePosting(@PathVariable("memberId") Long memberId,
                                    @PathVariable("postingId") Long postingId) {
        postingService.deletePosting(memberId, postingId);
        return ApiResponse.onSuccess(postingId);
    }

    @Operation(summary = "좋아요 취소하기", description = "게시글에 좋아요를 취소합니다.")
    @DeleteMapping("/api/postings/{postingId}/dislike?id={myId}")
    ApiResponse<String> dislikePosting(@PathVariable("postingId") Long postingId, @PathVariable("myId") Long myId) {
        postingService.unlikePosting(myId, postingId);
        return ApiResponse.onSuccess("게시글에 좋아요를 제거했습니다.");
    }
}