package euclid.lyc_spring.service.point;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.apiPayload.exception.handler.PointHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.PointUsage;
import euclid.lyc_spring.dto.response.PointResDTO;
import euclid.lyc_spring.repository.MemberRepository;
import euclid.lyc_spring.repository.PointUsageRepository;
import kr.co.bootpay.Bootpay;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PointServiceImpl implements PointService {

    private final MemberRepository memberRepository;
    private final PointUsageRepository pointUsageRepository;

    @Value("${bootpay.rest-api-key}")
    private String restApplicationId;

    @Value("${bootpay.private-key}")
    private String privateKey;

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

    @Override
    public PointResDTO.ReceiptDTO rechargePoints(String receiptId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        // 결제 단건 조회
        Map<String, Object> res = getReceipt(receiptId);

        // 포인트 충전
        member.addPoints((Integer) res.get("price"));
        memberRepository.save(member);

        return PointResDTO.ReceiptDTO.toDTO(member, res);
    }

    private Map<String, Object> getReceipt(String receiptId) {
        Map<String, Object> res;
        try {
            Bootpay bootpay = new Bootpay(restApplicationId, privateKey);

            // 액세스 토큰 발급 실패
            Map<String, Object> token = bootpay.getAccessToken();
            if(token.get("error_code") != null) {
                System.out.println(token.get("message"));
                throw new PointHandler(ErrorStatus.BOOTPAY_TOKEN_ISSUANCE_ERROR);
            }

            // 결제 조회 실패
            res = bootpay.getReceipt(receiptId);
            if (res.get("error_code") != null) {
                throw new PointHandler(ErrorStatus.BOOTPAY_RETRIEVE_PAYMENT_ERROR);
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new PointHandler(ErrorStatus.BOOT_PAY_SERVER_ERROR);
        }
        return res;
    }
}
