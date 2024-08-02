package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.info.InfoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InfoMaterialRepository extends JpaRepository<InfoMaterial, Long> {
}
