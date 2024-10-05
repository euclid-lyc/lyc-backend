package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.PushSet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PushSetRepository extends JpaRepository<PushSet, Long> {
}
