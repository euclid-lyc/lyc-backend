package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.info.Info;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InfoRepository extends JpaRepository<Info, Long> {

    Optional<Info> findByMemberId(Long memberId);
}
