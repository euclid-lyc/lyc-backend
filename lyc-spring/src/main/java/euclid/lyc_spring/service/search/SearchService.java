package euclid.lyc_spring.service.search;

import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.dto.response.SearchResponseDTO;

import java.util.List;

public interface SearchService {
    SearchResponseDTO.MemberPreViewListDTO searchDirectorByGenMode(String term);
    SearchResponseDTO.MemberKeywordPreviewListDTO searchDirectorByKeywordMode(List<String> term, String category);
}
