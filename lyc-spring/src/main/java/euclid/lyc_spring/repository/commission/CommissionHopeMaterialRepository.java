package euclid.lyc_spring.repository.commission;

import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionHopeMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionHopeMaterialRepository extends JpaRepository<CommissionHopeMaterial, Long> {

    void deleteAllByCommission(Commission commission);
}
