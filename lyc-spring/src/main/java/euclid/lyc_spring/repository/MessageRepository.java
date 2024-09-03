package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.chat.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    Optional<Message> findByIdAndIsText(Long imageId, boolean isText);

    Optional<Message> findByMemberChatChatIdAndCreatedAt(Long chatId, LocalDateTime createdAt);
}

