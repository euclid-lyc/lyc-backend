package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.chat.Chat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long> {

    Optional<Chat> findByIdAndInactive(Long id, LocalDateTime localDateTime);
}
