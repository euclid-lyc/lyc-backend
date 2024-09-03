package euclid.lyc_spring.service.chat;

import euclid.lyc_spring.dto.response.ChatResponseDTO;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;

public interface ChatQueryService {

/*-------------------------------------------------- 채팅방 --------------------------------------------------*/
    ChatResponseDTO.ChatPreviewListDTO getAllChats(PageRequest pageRequest);

    ChatResponseDTO.ChatMemberListDTO getChatMembers(Long chatId);

/*-------------------------------------------------- 메시지 --------------------------------------------------*/
/*-------------------------------------------------- 일정 --------------------------------------------------*/

    ChatResponseDTO.ScheduleListDTO getSchedules(Long chatId, Integer year, Integer month);

    ChatResponseDTO.ScheduleListDTO getSchedules(Long chatId, Integer year, Integer month, Integer day);

/*-------------------------------------------------- 사진 및 동영상 --------------------------------------------------*/

    ChatResponseDTO.ImageListDTO getAllChatImages(Long chatId, PageRequest pageRequest, LocalDateTime cursorDateTime);

    ChatResponseDTO.ChatImageDTO getChatImage(Long chatId, Long imageId);
}
