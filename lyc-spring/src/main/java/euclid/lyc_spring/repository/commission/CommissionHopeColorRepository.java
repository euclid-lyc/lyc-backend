package euclid.lyc_spring.repository.commission;

import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionHopeColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionHopeColorRepository extends JpaRepository<CommissionHopeColor, Long> {

    void deleteAllByCommission(Commission commission);
}
