package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.CommissionClothes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommissionClotheRepository extends JpaRepository<CommissionClothes, Long> {
    Optional<CommissionClothes> findCommissionClothesByIdAndChat(Long id, Chat chat);
}
