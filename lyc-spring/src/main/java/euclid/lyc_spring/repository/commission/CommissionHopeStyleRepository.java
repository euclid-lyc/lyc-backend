package euclid.lyc_spring.repository.commission;

import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionHopeStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionHopeStyleRepository extends JpaRepository<CommissionHopeStyle, Long> {

    void deleteAllByCommission(Commission commission);
}
