package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.dto.response.SearchResponseDTO;

import java.util.List;

public interface MemberRepositoryCustom {

    List<SearchResponseDTO.MemberPreviewDTO> searchDirectorByGenMode(String term);
    List<SearchResponseDTO.MemberKeywordPreviewDTO> searchDirectorByKeywordMode(List<String> term, String orderType);
}
