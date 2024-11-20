package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.dto.response.ChatResponseDTO;

import java.util.List;

public interface ChatRepositoryCustom {


    List<ChatResponseDTO.ChatPreviewDTO> searchChats(String keyword, Long loginId);
}
