package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.request.ImageRequestDTO;
import euclid.lyc_spring.dto.request.PostingRequestDTO;
import euclid.lyc_spring.dto.response.CommissionDTO;
import euclid.lyc_spring.dto.response.PostingDTO;
import euclid.lyc_spring.dto.response.WeatherDTO;
import euclid.lyc_spring.service.posting.PostingCommandService;
import euclid.lyc_spring.service.posting.PostingQueryService;
import euclid.lyc_spring.service.s3.S3ImageService;
import euclid.lyc_spring.service.social.WeatherService;
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
    private final WeatherService weatherService;

/*-------------------------------------------------- 피드 --------------------------------------------------*/

    @Operation(summary = "[구현완료] 게시글 미리보기 10개 불러오기", description = "홈 화면에 노출할 최신 피드 10개를 불러옵니다.")
    @GetMapping("/feeds/preview")
    public ApiResponse<PostingDTO.RecentPostingListDTO> getRecentPostings() {
        PostingDTO.RecentPostingListDTO recentPostingListDTO = postingQueryService.getRecentPostings();
        return ApiResponse.onSuccess(SuccessStatus._RECENT_TEN_FEEDS_FETCHED, recentPostingListDTO);
    }

    @Operation(summary = "[구현완료] 날씨 기반 추천 게시글 10개 불러오기", description = "피드 화면에 노출할 날씨 기반 추천 게시글 10개를 불러옵니다.")
    @GetMapping("/feeds/by-weather")
    public ApiResponse<PostingDTO.RecentPostingListDTO> getPostingsAccordingToWeather(@RequestParam String city) {
        WeatherDTO weatherDTO = weatherService.getTodayWeather(city);
        PostingDTO.RecentPostingListDTO postingListDTO = postingQueryService.getPostingsAccordingToWeather(weatherDTO);
        return ApiResponse.onSuccess(SuccessStatus._FEEDS_BY_WEATHER_FOUND, postingListDTO);
    }

    @Operation(summary = "[구현완료] 회원 맞춤 추천 게시글 목록 불러오기", description = """
    피드 화면에 노출할 회원 맞춤 추천 게시글 목록을 불러옵니다.
    
    커서 기반 페이징을 사용합니다. (커서가 2개 -> cursorScore, cursorId)
    """)
    @GetMapping("feeds/for-member")
    public ApiResponse<PostingDTO.RecommendedPostingListDTO> getPostingsForMember(
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam(required = false) Long cursorScore,
            @RequestParam(required = false) Long cursorId) {
        PostingDTO.RecommendedPostingListDTO recommendedPostingListDTO = postingQueryService.getPostingsForMember(pageSize, cursorScore, cursorId);
        return ApiResponse.onSuccess(SuccessStatus._FEEDS_FOR_MEMBER_FOUND, recommendedPostingListDTO);
    }

/*-------------------------------------------------- 게시글 공통 --------------------------------------------------*/

    @Operation(summary = "[구현완료] 게시글(코디 or 리뷰) 작성하기", description = "게시글을 작성합니다.")
    @PostMapping("/postings")
    public ApiResponse<PostingDTO.PostingViewDTO> createPosting(
            @RequestBody PostingRequestDTO.PostingSaveDTO postingSaveDTO,
            @RequestParam(required = false) Long commissionId) {
        PostingDTO.PostingViewDTO postingViewDTO = postingCommandService.createPosting(postingSaveDTO, commissionId);
        return ApiResponse.onSuccess(SuccessStatus._POSTING_CREATED, postingViewDTO);
    }

    @Operation(summary = "[구현완료] 게시글(코디 or 리뷰) 작성하기 - 이미지 업로드", description = "게시글을 작성합니다.")
    @PostMapping(value = "/postings/{postingId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<PostingDTO.PostingViewDTO> createPostingImage(
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
    public ApiResponse<PostingDTO.PostingIdDTO> deletePosting(@PathVariable("postingId") Long postingId) {
        PostingDTO.PostingIdDTO postingIdDTO = postingCommandService.deletePosting(postingId);
        return ApiResponse.onSuccess(SuccessStatus._POSTING_DELETED, postingIdDTO);
    }

    @Operation(summary = "[구현완료] 게시글 불러오기", description = "게시글(코디 or 리뷰)을 불러옵니다.")
    @GetMapping("/postings/{postingId}")
    public ApiResponse<PostingDTO.PostingViewDTO> getPosting(@PathVariable("postingId") Long postingId){
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
    public ApiResponse<PostingDTO.SavedPostingIdDTO> deleteSavedPosting(
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
    public ApiResponse<PostingDTO.PostingImageListDTO> getAllSavedCoordies(
            @PathVariable("memberId") Long memberId,
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam LocalDateTime cursorDateTime) {
        PostingDTO.PostingImageListDTO postingImageListDTO = postingQueryService.getAllSavedPostings(memberId, pageSize, cursorDateTime);
        return ApiResponse.onSuccess(SuccessStatus._SAVED_COORDIES_FETCHED, postingImageListDTO);
    }

    @Operation(summary = "[구현완료] 게시글 저장 여부 불러오기", description= """
            로그인한 회원의 게시글 저장 여부를 반환합니다.
            """)
    @GetMapping("/postings/{postingId}/save-status")
    public ApiResponse<Boolean> getPostingSaveStatus(@PathVariable Long postingId) {
        Boolean isSaved = postingQueryService.getPostingSaveStatus(postingId);
        return ApiResponse.onSuccess(SuccessStatus._POSTING_SAVE_STATUS_FOUND, isSaved);
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

    @Operation(summary = "[구현완료] 게시글 좋아요 여부 불러오기", description= """
            로그인한 회원의 게시글 좋아요 여부를 반환합니다.
            """)
    @GetMapping("/postings/{postingId}/like-status")
    public ApiResponse<Boolean> getPostingLikeStatus(@PathVariable Long postingId) {
        Boolean isLiked = postingQueryService.getPostingLikeStatus(postingId);
        return ApiResponse.onSuccess(SuccessStatus._POSTING_LIKE_STATUS_FOUND, isLiked);
    }


/*-------------------------------------------------- 코디 게시글 --------------------------------------------------*/


    @Operation(summary = "[구현완료] 유저의 코디 목록 불러오기", description = """
            마이페이지에 유저의 코디 목록을 불러옵니다.
            
            커서 기반 페이징이 적용됩니다. cursorDateTime은 이전에 전달된 마지막 <코디>의 업로드 시각입니다.
            
            이 API는 cursorDateTime보다 이전에 업로드된 <코디>의 목록을 불러옵니다.
            """)
    @GetMapping("/members/{memberId}/coordies")
    public ApiResponse<PostingDTO.PostingImageListDTO> getAllMemberCoordies(
            @PathVariable("memberId") Long memberId,
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam LocalDateTime cursorDateTime) {
        PostingDTO.PostingImageListDTO postingImageListDTO = postingQueryService.getAllMemberCoordies(memberId, pageSize, cursorDateTime);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_COORDIES_FETCHED, postingImageListDTO);
    }

/*-------------------------------------------------- 리뷰 게시글 --------------------------------------------------*/

    @Operation(summary = "[구현완료] 작성 가능한 리뷰 목록 불러오기", description = """
            30일 이내에 종료된 의뢰 중 아직 리뷰를 작성하지 않은 의뢰의 목록을 불러옵니다.
            
            커서 기반 페이징이 적용됩니다.
            
            커서 1 : cursorDateTime (이전에 전달된 마지막 <의뢰>의 종료 시각)
            
            커서 2 : cursorId (이전에 전달된 마지막 <의뢰>의 id)
            
            이 API는 cursorDateTime보다 이전에 종료된 <의뢰>의 목록을 불러옵니다.
            """)
    @GetMapping("/reviews/directors")
    public ApiResponse<CommissionDTO.TerminatedCommissionListDTO> getReviewsAvailableForSubmission(
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam(required = false) LocalDateTime cursorDateTime,
            @RequestParam(required = false) Long cursorId) {
        CommissionDTO.TerminatedCommissionListDTO terminatedCommissionListDTO = postingQueryService.getReviewsAvailableForSubmission(pageSize, cursorDateTime, cursorId);
        return ApiResponse.onSuccess(SuccessStatus._REVIEWS_AVAILABLE_FOR_SUBMISSION_FOUND, terminatedCommissionListDTO);
    }

    @Operation(summary = "[구현완료] 유저의 리뷰 목록 불러오기", description = """
            마이페이지에 유저의 리뷰 목록을 불러옵니다.
            
            커서 기반 페이징이 적용됩니다. cursorDateTime은 이전에 전달된 마지막 <리뷰>의 업로드 시각입니다.
            
            이 API는 cursorDateTime보다 이전에 업로드된 <리뷰>의 목록을 불러옵니다.
            """)
    @GetMapping("/members/{memberId}/reviews")
    public ApiResponse<PostingDTO.PostingImageListDTO> getAllMemberReviews(
            @PathVariable("memberId") Long memberId,
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam LocalDateTime cursorDateTime) {
        PostingDTO.PostingImageListDTO postingImageListDTO = postingQueryService.getAllMemberReviews(memberId, pageSize, cursorDateTime);
        return ApiResponse.onSuccess(SuccessStatus._MEMBER_REVIEWS_FETCHED, postingImageListDTO);
    }
}
