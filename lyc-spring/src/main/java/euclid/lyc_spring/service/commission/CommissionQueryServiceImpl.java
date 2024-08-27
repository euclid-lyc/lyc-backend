package euclid.lyc_spring.service.commission;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.CommissionHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.commission.Commission;
import euclid.lyc_spring.dto.response.CommissionDTO;
import euclid.lyc_spring.repository.CommissionRepository;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommissionQueryServiceImpl implements CommissionQueryService {


    private final MemberRepository memberRepository;
    private final CommissionRepository commissionRepository;

    @Override
    public List<CommissionDTO.CommissionViewDTO> getAllCommissionList(Long directorId) {

        Member director = memberRepository.findById(directorId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<Commission> commissionList = commissionRepository.findByDirector(director);
        return commissionList.stream()
                .map(CommissionDTO.CommissionViewDTO::toDTO)
                .toList();
    }

    @Override
    public CommissionDTO.CommissionInfoDTO getCommission(Long commissionId) {

        Commission commission = commissionRepository.findById(commissionId)
                .orElseThrow(() -> new CommissionHandler(ErrorStatus.COMMISSION_NOT_FOUND));

        return CommissionDTO.CommissionInfoDTO.toDTO(commission);
    }
}
