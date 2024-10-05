package euclid.lyc_spring.service.chat;

import euclid.lyc_spring.dto.request.ChatRequestDTO;
import euclid.lyc_spring.dto.response.ChatResponseDTO;

public interface ChatCommandService {

/*-------------------------------------------------- 채팅방 --------------------------------------------------*/

    ChatResponseDTO.ChatInactiveDTO terminateChat(Long chatId);

/*-------------------------------------------------- 메시지 --------------------------------------------------*/
/*-------------------------------------------------- 일정 --------------------------------------------------*/

    ChatResponseDTO.ScheduleDTO createSchedule(Long chatId, ChatRequestDTO.ScheduleReqDTO scheduleReqDTO);

    ChatResponseDTO.MessageInfoDTO saveMessage(Long chatId, ChatRequestDTO.MessageDTO messageDTO);

    /*-------------------------------------------------- 사진 및 동영상 --------------------------------------------------*/

}
