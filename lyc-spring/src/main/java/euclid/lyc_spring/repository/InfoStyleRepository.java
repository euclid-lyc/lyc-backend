package euclid.lyc_spring.repository;

import euclid.lyc_spring.domain.info.InfoStyle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InfoStyleRepository extends JpaRepository<InfoStyle, Long> {

    void deleteAllByInfoId(Long infoId);

    List<InfoStyle> findAllByInfoId(Long infoId);
}
