package euclid.lyc_spring.service.chat;

import euclid.lyc_spring.dto.response.ChatResponseDTO;

public interface ChatQueryService {

/*-------------------------------------------------- 채팅방 --------------------------------------------------*/
    ChatResponseDTO.ChatPreviewListDTO getAllChats();

    ChatResponseDTO.ChatMemberListDTO getChatMembers(Long chatId);

/*-------------------------------------------------- 메시지 --------------------------------------------------*/
/*-------------------------------------------------- 일정 --------------------------------------------------*/
/*-------------------------------------------------- 사진 및 동영상 --------------------------------------------------*/
}
