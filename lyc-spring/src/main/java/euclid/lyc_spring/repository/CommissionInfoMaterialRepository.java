package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionInfo;
import euclid.lyc_spring.domain.chat.commission.commission_info.CommissionInfoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommissionInfoMaterialRepository extends JpaRepository<CommissionInfoMaterial, Long> {
    void deleteAllByCommissionInfo(CommissionInfo commissionInfo);
}
