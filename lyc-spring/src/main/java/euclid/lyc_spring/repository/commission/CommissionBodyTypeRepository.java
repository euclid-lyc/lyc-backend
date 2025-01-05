package euclid.lyc_spring.repository.commission;

import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionBodyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionBodyTypeRepository extends JpaRepository<CommissionBodyType, Long> {
    void deleteAllByCommission(Commission commission);
}
