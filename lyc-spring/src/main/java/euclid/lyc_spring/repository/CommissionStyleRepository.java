package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommissionStyleRepository extends JpaRepository<CommissionStyle, Long> {

    Optional<CommissionStyle> findByCommission(Commission commission);

    void deleteAllByCommission(Commission commission);
}
