package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.chat.ImageMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageMessageRepository extends JpaRepository<ImageMessage, Long> {
}
