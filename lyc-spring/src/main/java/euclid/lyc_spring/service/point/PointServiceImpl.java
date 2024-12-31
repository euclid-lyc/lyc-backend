package euclid.lyc_spring.service.point;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.PointUsage;
import euclid.lyc_spring.dto.response.PointResDTO;
import euclid.lyc_spring.repository.MemberRepository;
import euclid.lyc_spring.repository.PointUsageRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final MemberRepository memberRepository;
    private final PointUsageRepository pointUsageRepository;

    @Override
    public PointResDTO.MemberPointDTO getMyPoints() {
        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        return PointResDTO.MemberPointDTO.toDTO(member);
    }

    @Override
    public PointResDTO.UsageListDTO getMyPointUsages(Integer pageSize, LocalDateTime cursorCreatedAt) {
        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<PointUsage> pointUsages = pointUsageRepository.findMemberPointUsages(member.getId(), pageSize, cursorCreatedAt);
        return PointResDTO.UsageListDTO.toDTO(member, pointUsages);
    }
}
