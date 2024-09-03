package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.domain.chat.Message;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface MemberChatRepositoryCustom {

    List<ChatResponseDTO.LatestMessageDTO> findAllChatsByMemberId(Long memberId, Pageable pageable);

    List<ChatResponseDTO.MemberPreviewDTO> findPartnerByChatAndMemberId(Long chatId, Long memberId);

    List<Message> findImageMessagesSortedByCreatedAt(Long memberId, Pageable pageable, LocalDateTime cursorDateTime);
}
