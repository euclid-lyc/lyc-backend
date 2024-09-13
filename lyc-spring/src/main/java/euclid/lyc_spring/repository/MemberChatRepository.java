package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.mapping.MemberChat;
import euclid.lyc_spring.repository.querydsl.MemberChatRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberChatRepository extends JpaRepository<MemberChat, Long>, MemberChatRepositoryCustom {

    List<MemberChat> findAllByMemberId(Long id);

    boolean existsByMemberIdAndChatId(Long id, Long chatId);

    Optional<MemberChat> findByMemberIdAndChatId(Long id, Long chatId);

    List<MemberChat> findAllByChatId(Long chatId);

    // 더미데이터용
    Optional<MemberChat> findById(Long chatId);
}
