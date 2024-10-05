package euclid.lyc_spring.repository.querydsl;

import euclid.lyc_spring.domain.chat.commission.Commission;

import java.time.LocalDateTime;
import java.util.List;

public interface CommissionRepositoryCustom {

    List<Commission> findCommissionsByDirectorId(Long directorId, Integer pageSize, LocalDateTime cursorDateTime);

    List<Commission> findUnreviewedCommissions(Integer pageSize, LocalDateTime cursorDateTime, Long cursorId);
}
