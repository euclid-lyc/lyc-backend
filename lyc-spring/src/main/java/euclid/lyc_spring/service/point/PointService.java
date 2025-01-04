package euclid.lyc_spring.service.point;

import euclid.lyc_spring.dto.response.PointResDTO;

import java.time.LocalDateTime;

public interface PointService {

    PointResDTO.MemberPointDTO getMyPoints();

    PointResDTO.UsageListDTO getMyPointUsages(Integer pageSize, LocalDateTime cursorCreatedAt);

    PointResDTO.ReceiptDTO rechargePoints(String receiptId);
}
