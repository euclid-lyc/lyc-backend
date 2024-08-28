package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.info.InfoBodyType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfoBodyTypeRepository extends JpaRepository<InfoBodyType, Long> {

    void deleteAllByInfoId(Long infoId);
}