package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CommissionInfoRepository extends JpaRepository<CommissionInfo, Long> {
    Optional<CommissionInfo> findByCommission(Commission commission);
}
