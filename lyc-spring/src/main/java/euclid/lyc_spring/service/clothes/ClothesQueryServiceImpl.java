package euclid.lyc_spring.service.clothes;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ClothesHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.clothes.Clothes;
import euclid.lyc_spring.dto.response.ClothesDTO;
import euclid.lyc_spring.repository.ClothesImageRepository;
import euclid.lyc_spring.repository.ClothesRepository;
import euclid.lyc_spring.repository.ClothesTextRepository;
import euclid.lyc_spring.repository.MemberRepository;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClothesQueryServiceImpl implements ClothesQueryService {

    private final MemberRepository memberRepository;
    private final ClothesRepository clothesRepository;
    private final ClothesImageRepository clothesImageRepository;
    private final ClothesTextRepository clothesTextRepository;

    @Override
    public ClothesDTO.ClothesListDTO getClothesList(Long memberId, Integer pageSize, LocalDateTime cursorDateTime) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<ClothesDTO.ClothesInfoDTO> clothesInfoDTOList = clothesRepository.findClothesByMemberId(memberId, pageSize, cursorDateTime).stream()
                .map(ClothesDTO.ClothesInfoDTO::toDTO)
                .toList();

        return ClothesDTO.ClothesListDTO.builder()
                .memberId(memberId)
                .clothesList(clothesInfoDTOList)
                .build();
    }

    @Override
    public ClothesDTO.ClothesViewDTO getClothes(Long clothesId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new ClothesHandler(ErrorStatus.CLOTHES_NOT_FOUND));

        return ClothesDTO.ClothesViewDTO.toDTO(clothes);
    }

    @Override
    public boolean getIsText(Long clothesId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new ClothesHandler(ErrorStatus.CLOTHES_NOT_FOUND));

        return clothes.getIsText();
    }

    @Override
    public ClothesDTO.ClothesWithTextDTO getClothesUploadedWithText(Long clothesId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new ClothesHandler(ErrorStatus.CLOTHES_NOT_FOUND));

        return ClothesDTO.ClothesWithTextDTO.toDTO(clothes);
    }

    @Override
    public ClothesDTO.ClothesWithImageDTO getClothesUploadedWithImage(Long clothesId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new ClothesHandler(ErrorStatus.CLOTHES_NOT_FOUND));

        return ClothesDTO.ClothesWithImageDTO.toDTO(clothes);
    }
}
