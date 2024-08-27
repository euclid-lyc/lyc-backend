package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionStyle;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionStyleColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionStyleColorRepository extends JpaRepository<CommissionStyleColor, Long> {
    void deleteAllByCommissionStyle(CommissionStyle commissionStyle);
}
