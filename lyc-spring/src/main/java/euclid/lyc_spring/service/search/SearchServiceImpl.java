package euclid.lyc_spring.service.search;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.apiPayload.exception.handler.PostingHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import euclid.lyc_spring.dto.response.PostingDTO;
import euclid.lyc_spring.dto.response.SearchResponseDTO;
import euclid.lyc_spring.repository.ChatRepository;
import euclid.lyc_spring.repository.MemberRepository;
import euclid.lyc_spring.repository.PostingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SearchServiceImpl implements SearchService {

    private final MemberRepository memberRepository;
    private final PostingRepository postingRepository;
    private final ChatRepository chatRepository;

    @Override
    public ChatResponseDTO.ChatPreviewListDTO searchChat(String keyword) {
        Member member = authorization();

        List<ChatResponseDTO.ChatPreviewDTO> chatPreviewDTOs = chatRepository.searchChats(keyword, member.getId());

        return ChatResponseDTO.ChatPreviewListDTO.toDTO(chatPreviewDTOs);
    }

    @Override
    public SearchResponseDTO.MemberPreViewListDTO searchDirectorByGenMode(String term) {
        Member member = authorization();

        List<SearchResponseDTO.MemberPreviewDTO> memberPreviewDTOS = memberRepository.searchDirectorByGenMode(term);
        if(memberPreviewDTOS.isEmpty()){
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }
        return SearchResponseDTO.MemberPreViewListDTO.toDTO(memberPreviewDTOS);
    }

    public SearchResponseDTO.MemberKeywordPreviewListDTO searchDirectorByKeywordMode(List<String> term, String category) {
        Member member = authorization();

        List<SearchResponseDTO.MemberKeywordPreviewDTO> memberPreviewDTOS = memberRepository.searchDirectorByKeywordMode(term,category);

        if(memberPreviewDTOS.isEmpty()){
            throw new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND);
        }

        return SearchResponseDTO.MemberKeywordPreviewListDTO.toDTO(memberPreviewDTOS);
    }

    @Override
    public PostingDTO.PostingImageListDTO searchPosting(String keyword, String orderType) {
        Member member = authorization();

        List<PostingDTO.PostingImageDTO> postingImageDTOList = postingRepository.searchPosting(keyword, orderType);

        if(postingImageDTOList.isEmpty()){
            throw new PostingHandler(ErrorStatus.POSTING_NOT_FOUND);
        }

        return PostingDTO.PostingImageListDTO.toDTO(postingImageDTOList);
    }

    private Member authorization(){
        String loginId = SecurityUtils.getAuthorizedLoginId();
        return memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
    }
}
