package euclid.lyc_spring.service.posting;

import euclid.lyc_spring.dto.response.PostingDTO;

import java.time.LocalDateTime;

public interface PostingQueryService {

/*-------------------------------------------------- 피드 --------------------------------------------------*/

    PostingDTO.RecentPostingListDTO getRecentPostings();

/*-------------------------------------------------- 게시글 공통 --------------------------------------------------*/

    PostingDTO.PostingViewDTO getPosting(Long memberId);
    PostingDTO.PostingImageListDTO getAllSavedPostings(Long memberId, Integer pageSize, LocalDateTime cursorDateTime);


/*-------------------------------------------------- 코디 게시글 --------------------------------------------------*/

    PostingDTO.PostingImageListDTO getAllMemberCoordies(Long memberId);

/*-------------------------------------------------- 리뷰 게시글 --------------------------------------------------*/

    PostingDTO.PostingImageListDTO getAllMemberReviews(Long memberId);

}
