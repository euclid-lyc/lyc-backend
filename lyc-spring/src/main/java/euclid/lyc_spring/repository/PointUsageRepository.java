package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.PointUsage;
import euclid.lyc_spring.repository.querydsl.PointUsageRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PointUsageRepository extends JpaRepository<PointUsage, Long>, PointUsageRepositoryCustom {
}
