package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.info.InfoFit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Arrays;
import java.util.List;

@Repository
public interface InfoFitRepository extends JpaRepository<InfoFit, Long> {

    void deleteAllByInfoId(Long infoId);

    List<InfoFit> findAllByInfoId(Long infoId);
}