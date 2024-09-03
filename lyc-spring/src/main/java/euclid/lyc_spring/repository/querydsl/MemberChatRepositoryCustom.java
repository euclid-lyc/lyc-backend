package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.dto.response.ChatResponseDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MemberChatRepositoryCustom {

    List<ChatResponseDTO.LatestMessageDTO> findAllChatsByMemberId(Long memberId, Pageable pageable);

    List<ChatResponseDTO.MemberPreviewDTO> findPartnerByChatAndMemberId(Long chatId, Long memberId);
}
