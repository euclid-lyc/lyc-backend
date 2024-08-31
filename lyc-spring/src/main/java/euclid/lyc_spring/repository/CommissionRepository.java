package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.commission.Commission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommissionRepository extends JpaRepository<Commission, Long> {
    public List<Commission> findByDirector(Member director);
    public Optional<Commission> findById(Long commissionId);
    public Optional<Commission> findByChat(Chat chat);
}