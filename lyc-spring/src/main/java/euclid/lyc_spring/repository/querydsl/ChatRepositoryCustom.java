package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.dto.response.ChatResponseDTO;

import java.util.List;
import java.util.Optional;

public interface ChatRepositoryCustom {


    List<ChatResponseDTO.ChatPreviewDTO> searchChats(String keyword, Long loginId);

    Optional<Chat> findByChatMembers(Long id1, Long id2);
}
