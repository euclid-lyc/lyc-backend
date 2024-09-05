package euclid.lyc_spring.service.commission;

import euclid.lyc_spring.dto.response.CommissionDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface CommissionQueryService {
    public List<CommissionDTO.CommissionViewDTO> getAllCommissionList(Long directorId, Integer pageSize, LocalDateTime cursorDateTime);
    public CommissionDTO.CommissionInfoDTO getCommission(Long commissionId);
}
