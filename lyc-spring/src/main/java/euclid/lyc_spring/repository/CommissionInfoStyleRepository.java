package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionInfo;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionInfoStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionInfoStyleRepository extends JpaRepository<CommissionInfoStyle, Long> {
    void deleteAllByCommissionInfo(CommissionInfo commissionInfo);
}
