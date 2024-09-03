package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.request.ImageRequestDTO;
import euclid.lyc_spring.dto.request.PostingRequestDTO;
import euclid.lyc_spring.dto.response.PostingDTO;
import euclid.lyc_spring.service.posting.PostingCommandService;
import euclid.lyc_spring.service.posting.PostingQueryService;
import euclid.lyc_spring.service.s3.S3ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Tag(name = "Posting", description = "게시글 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc")
public class PostingController {

    private final PostingQueryService postingQueryService;
    private final PostingCommandService postingCommandService;
    private final S3ImageService s3ImageService;

/*-------------------------------------------------- 피드 --------------------------------------------------*/

    @Operation(summary = "[구현완료] 게시글 미리보기 10개 불러오기", description = "홈 화면에 노출할 최신 피드 10개를 불러옵니다.")
    @GetMapping("/feeds/preview")
    ApiResponse<PostingDTO.RecentPostingListDTO> getRecentPostings() {
        PostingDTO.RecentPostingListDTO recentPostingListDTO = postingQueryService.getRecentPostings();
        return ApiResponse.onSuccess(SuccessStatus._RECENT_TEN_FEEDS_FETCHED, recentPostingListDTO);
    }

    @Operation(summary = "[구현중] 날씨 기반 추천 게시글 10개 불러오기", description = "피드 화면에 노출할 날씨 기반 추천 게시글 10개를 불러옵니다.")
    @GetMapping("/feeds/by-weather")
    void getPostingsAccordingToWeather() {}

    @Operation(summary = "[구현중] 회원 맞춤 추천 게시글 목록 불러오기", description = """
    피드 화면에 노출할 회원 맞춤 추천 게시글 목록을 불러옵니다.
    
    오프셋 기반 페이징을 사용합니다.
    """)
    @GetMapping("feeds/for-member")
    void getPostingForMember() {}

/*-------------------------------------------------- 게시글 공통 --------------------------------------------------*/

    @Operation(summary = "[구현완료] 게시글(코디 or 리뷰) 작성하기", description = "게시글을 작성합니다.")
    @PostMapping("/postings")
    ApiResponse<PostingDTO.PostingViewDTO> createPosting(
            @RequestBody PostingRequestDTO.PostingSaveDTO postingSaveDTO) {
        PostingDTO.PostingViewDTO postingViewDTO = postingCommandService.createPosting(postingSaveDTO);
        return ApiResponse.onSuccess(SuccessStatus._POSTING_CREATED, postingViewDTO);
    }

    @Operation(summary = "[구현완료] 게시글(코디 or 리뷰) 작성하기 - 이미지 업로드", description = "게시글을 작성합니다.")
    @PostMapping(value = "/postings/{postingId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<PostingDTO.PostingViewDTO> createPostingImage(
            @PathVariable Long postingId,
            @RequestPart ImageRequestDTO.LinkDTO linkDTO,
            @RequestPart(required = false) List<MultipartFile> multipartFiles) {
            List<String> images = multipartFiles.stream()
                    .map(s3ImageService::upload)
                    .toList();
        PostingDTO.PostingViewDTO postingViewDTO = postingCommandService.createPostingImage(postingId, linkDTO.getLinks(), images);
        return ApiResponse.onSuccess(SuccessStatus._POSTING_CREATED, postingViewDTO);
    }

    @Operation(summary = "[구현완료] 게시글(코디 or 리뷰) 삭제하기", description = "게시글을 삭제합니다.")
    @DeleteMapping("/postings/{postingId}")
    ApiResponse<PostingDTO.PostingIdDTO> deletePosting(@PathVariable("postingId") Long postingId) {
        PostingDTO.PostingIdDTO postingIdDTO = postingCommandService.deletePosting(postingId);
        return ApiResponse.onSuccess(SuccessStatus._POSTING_DELETED, postingIdDTO);
    }

    @Operation(summary = "[구현완료] 게시글 불러오기", description = "게시글(코디 or 리뷰)을 불러옵니다.")
    @GetMapping("/postings/{postingId}")
    ApiResponse<PostingDTO.PostingViewDTO> getPosting(@PathVariable("postingId") Long postingId){
        PostingDTO.PostingViewDTO postingViewDTO = postingQueryService.getPosting(postingId);
        return ApiResponse.onSuccess(SuccessStatus._OK, postingViewDTO);
    }

    @Operation(summary = "[구현완료] 게시글 저장하기", description = "게시글을 저장합니다")
    @PostMapping("/postings/{postingId}/saved-postings")
    public ApiResponse<PostingDTO.PostingViewDTO> savedPosting(@PathVariable("postingId") Long postingId) {
        PostingDTO.PostingViewDTO postingViewDTO = postingCommandService.savePosting(postingId);
        return ApiResponse.onSuccess(SuccessStatus._SAVED_POSTING_CREATED, postingViewDTO);
    }

    @Operation(summary = "[구현완료] 게시글 저장 취소하기", description = "저장되어 있는 게시글을 삭제합니다.")
    @DeleteMapping("/postings/{postingId}/saved-postings")
    ApiResponse<PostingDTO.SavedPostingIdDTO> deleteSavedPosting(
            @PathVariable("postingId") Long postingId) {
        PostingDTO.SavedPostingIdDTO savedPostingIdDTO = postingCommandService.deleteSavedPosting(postingId);
        return ApiResponse.onSuccess(SuccessStatus._SAVED_POSTING_DELETED, savedPostingIdDTO);
    }

    @Operation(summary = "[구현완료] 저장한 코디 목록 불러오기", description = """
            마이페이지에 저장한 코디 목록을 불러옵니다.
            
            커서 기반 페이징이 적용됩니다. cursorDateTime은 이전에 전달된 마지막 <저장한 코디>의 업로드 시각입니다.
            
            이 API는 cursorDateTime보다 이전에 업로드된 <저장한 코디>의 목록을 불러옵니다.
            """)
    @GetMapping("/members/{memberId}/saved-postings")
    ApiResponse<PostingDTO.PostingImageListDTO> getAllSavedCoordies(
            @PathVariable("memberId") Long memberId,
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam LocalDateTime cursorDateTime) {
        PostingDTO.PostingImageListDTO postingImageListDTO = postingQueryService.getAllSavedPostings(memberId, pageSize, cursorDateTime);
        return ApiResponse.onSuccess(SuccessStatus._SAVED_COORDIES_FETCHED, postingImageListDTO);
    }

    @Operation(summary = "[구현완료] 게시글 좋아요 하기", description = "게시글에 좋아요를 누릅니다.")
    @PostMapping("/postings/{postingId}/likes")
    public ApiResponse<PostingDTO.PostingViewDTO> likePosting(@PathVariable("postingId") Long postingId) {
        PostingDTO.PostingViewDTO postingViewDTO = postingCommandService.likePosting(postingId);
        return ApiResponse.onSuccess(SuccessStatus._POSTING_LIKED, postingViewDTO);
    }

    @Operation(summary = "[구현완료] 게시글 좋아요 취소하기", description = "게시글에 좋아요를 취소합니다.")
    @PatchMapping("/postings/{postingId}/dislikes")
    public ApiResponse<PostingDTO.PostingViewDTO> dislikePosting(@PathVariable("postingId") Long postingId) {
        PostingDTO.PostingViewDTO postingViewDTO = postingCommandService.dislikePosting(postingId);
        return ApiResponse.onSuccess(SuccessStatus._POSTING_LIKE_CANCELED, postingViewDTO);
    }


/*-------------------------------------------------- 코디 게시글 --------------------------------------------------*/


    @Operation(summary = "[구현완료] 유저의 코디 목록 불러오기", description = """
            마이페이지에 유저의 코디 목록을 불러옵니다.
            
            커서 기반 페이징이 적용됩니다. cursorDateTime은 이전에 전달된 마지막 <코디>의 업로드 시각입니다.
            
            이 API는 cursorDateTime보다 이전에 업로드된 <코디>의 목록을 불러옵니다.
            """)
    @GetMapping("/members/{memberId}/coordies")
    ApiResponse<PostingDTO.PostingImageListDTO> getAllMemberCoordies(
            @PathVariable("memberId") Long memberId,
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam LocalDateTime cursorDateTime) {
        PostingDTO.PostingImageListDTO postingImageListDTO = postingQueryService.getAllMemberCoordies(memberId, pageSize, cursorDateTime);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_COORDIES_FETCHED, postingImageListDTO);
    }

/*-------------------------------------------------- 리뷰 게시글 --------------------------------------------------*/

    @Operation(summary = "[구현완료] 유저의 리뷰 목록 불러오기", description = """
            마이페이지에 유저의 리뷰 목록을 불러옵니다.
            
            커서 기반 페이징이 적용됩니다. cursorDateTime은 이전에 전달된 마지막 <리뷰>의 업로드 시각입니다.
            
            이 API는 cursorDateTime보다 이전에 업로드된 <리뷰>의 목록을 불러옵니다.
            """)
    @GetMapping("/members/{memberId}/reviews")
    ApiResponse<PostingDTO.PostingImageListDTO> getAllMemberReviews(
            @PathVariable("memberId") Long memberId,
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam LocalDateTime cursorDateTime) {
        PostingDTO.PostingImageListDTO postingImageListDTO = postingQueryService.getAllMemberReviews(memberId, pageSize, cursorDateTime);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_REVIEWS_FETCHED, postingImageListDTO);
    }
}
