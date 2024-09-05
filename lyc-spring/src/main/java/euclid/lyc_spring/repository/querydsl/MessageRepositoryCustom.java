package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.domain.chat.Message;

import java.time.LocalDateTime;
import java.util.List;

public interface MessageRepositoryCustom {

    List<Message> findMessagesByChatId(Long chatId, Integer pageSize, LocalDateTime cursorDateTime);
}
