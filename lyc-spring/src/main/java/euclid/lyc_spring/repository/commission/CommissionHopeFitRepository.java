package euclid.lyc_spring.repository.commission;

import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionHopeFit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionHopeFitRepository extends JpaRepository<CommissionHopeFit, Long> {

    void deleteAllByCommission(Commission commission);
}
