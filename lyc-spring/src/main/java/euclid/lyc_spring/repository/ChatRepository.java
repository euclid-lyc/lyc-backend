package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.repository.querydsl.ChatRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends JpaRepository<Chat, Long>, ChatRepositoryCustom {

    Optional<Chat> findByIdAndInactive(Long id, LocalDateTime localDateTime);

    List<Chat> findAllByInactiveBefore(LocalDateTime maxInactive);

}
