package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.chat.TextMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TextMessageRepository extends JpaRepository<TextMessage, Long> {
}
