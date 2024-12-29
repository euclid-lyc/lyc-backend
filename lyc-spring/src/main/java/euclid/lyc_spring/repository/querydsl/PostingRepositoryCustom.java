package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.dto.response.InfoResponseDTO;
import euclid.lyc_spring.dto.response.PostingDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface PostingRepositoryCustom {

    List<Posting> findCoordiesByFromMemberId(Long id, Integer pageSize, LocalDateTime cursorDateTime);

    List<Posting> findReviewsByToMemberId(Long memberId, Integer pageSize, LocalDateTime cursorDateTime);

    List<PostingDTO.PostingImageDTO> searchPosting(String keyword, String orderType);

    List<PostingDTO.PostingScoreDTO> findPostingsForMember(InfoResponseDTO.AllInfoDTO member, Integer pageSize, Long cursorScore, Long cursorId);

    List<Posting> findPostingsByWeather(Double minTemp, Double maxTemp, Long memberId);
}
