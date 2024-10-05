package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionStyle;
import euclid.lyc_spring.domain.chat.commission.commission_style.CommissionStyleMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionStyleMaterialRepository extends JpaRepository<CommissionStyleMaterial, Long> {
    void deleteAllByCommissionStyle(CommissionStyle commissionStyle);
}
