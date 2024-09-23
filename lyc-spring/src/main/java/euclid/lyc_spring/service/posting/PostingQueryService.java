package euclid.lyc_spring.service.posting;

import euclid.lyc_spring.dto.response.PostingDTO;

import java.time.LocalDateTime;

public interface PostingQueryService {

/*-------------------------------------------------- 피드 --------------------------------------------------*/

    PostingDTO.RecentPostingListDTO getRecentPostings();
    PostingDTO.RecommendedPostingListDTO getPostingsForMember(Integer pageSize, Long cursorScore, Long cursorId);

/*-------------------------------------------------- 게시글 공통 --------------------------------------------------*/

    PostingDTO.PostingViewDTO getPosting(Long memberId);
    PostingDTO.PostingImageListDTO getAllSavedPostings(Long memberId, Integer pageSize, LocalDateTime cursorDateTime);

/*-------------------------------------------------- 코디 게시글 --------------------------------------------------*/

    PostingDTO.PostingImageListDTO getAllMemberCoordies(Long memberId, Integer pageSize, LocalDateTime cursorDateTime);

/*-------------------------------------------------- 리뷰 게시글 --------------------------------------------------*/

    PostingDTO.PostingImageListDTO getAllMemberReviews(Long memberId, Integer pageSize, LocalDateTime cursorDateTime);
    Boolean getPostingLikeStatus(Long postingId);
    Boolean getPostingSaveStatus(Long postingId);

}
