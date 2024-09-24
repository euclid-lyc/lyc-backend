package euclid.lyc_spring.service.search;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.dto.response.SearchResponseDTO;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImpl implements SearchService {

    private final MemberRepository memberRepository;

    @Override
    public SearchResponseDTO.MemberPreViewListDTO searchDirectorByGenMode(String term) {
        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<SearchResponseDTO.MemberPreviewDTO> memberPreviewDTOS = memberRepository.searchDirectorByGenMode(term);
        if(memberPreviewDTOS.isEmpty()){
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }
        return SearchResponseDTO.MemberPreViewListDTO.toDTO(memberPreviewDTOS);
    }

    public SearchResponseDTO.MemberKeywordPreviewListDTO searchDirectorByKeywordMode(List<String> term, String category) {
        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<SearchResponseDTO.MemberKeywordPreviewDTO> memberPreviewDTOS = memberRepository.searchDirectorByKeywordMode(term,category);

        if(memberPreviewDTOS.isEmpty()){
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        return SearchResponseDTO.MemberKeywordPreviewListDTO.toDTO(memberPreviewDTOS);
    }
}
