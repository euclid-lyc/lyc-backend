package euclid.lyc_spring.service.posting;

import euclid.lyc_spring.dto.request.PostingRequestDTO;
import euclid.lyc_spring.dto.response.PostingDTO;

import java.util.List;

public interface PostingCommandService {
    /*-------------------------------------------------- 공통 --------------------------------------------------*/

    PostingDTO.PostingViewDTO createPosting(PostingRequestDTO.PostingSaveDTO postingSaveDTO);
    PostingDTO.PostingViewDTO createPostingImage(Long postingId, List<List<String>> links, List<String> images);
    PostingDTO.PostingIdDTO deletePosting(Long memberId);
    PostingDTO.PostingViewDTO savePosting(Long postingId);
    PostingDTO.SavedPostingIdDTO deleteSavedPosting(Long postingId);
    PostingDTO.PostingViewDTO likePosting(Long postingId);
    PostingDTO.PostingViewDTO dislikePosting(Long postingId);

    /*-------------------------------------------------- 코디 게시글 --------------------------------------------------*/

    /*-------------------------------------------------- 리뷰 게시글 --------------------------------------------------*/

}
