package euclid.lyc_spring.service.clothes;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ClothesHandler;
import euclid.lyc_spring.apiPayload.exception.handler.JwtHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.clothes.Clothes;
import euclid.lyc_spring.dto.response.ClothesDTO;
import euclid.lyc_spring.repository.ClothesImageRepository;
import euclid.lyc_spring.repository.ClothesRepository;
import euclid.lyc_spring.repository.ClothesTextRepository;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClothesQueryServiceImpl implements ClothesQueryService {

    private final MemberRepository memberRepository;
    private final ClothesRepository clothesRepository;
    private final ClothesImageRepository clothesImageRepository;
    private final ClothesTextRepository clothesTextRepository;

    @Override
    public ClothesDTO.ClothesListDTO getClothesList(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<ClothesDTO.ClothesInfoDTO> clothesInfoDTOList = clothesRepository.findByMember(member).stream()
                .map(ClothesDTO.ClothesInfoDTO::toDTO)
                .toList();

        return ClothesDTO.ClothesListDTO.builder()
                .memberId(memberId)
                .clothesList(clothesInfoDTOList)
                .build();
    }

    @Override
    public ClothesDTO.ClothesViewDTO getClothes(Long clothesId) {

        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new ClothesHandler(ErrorStatus.CLOTHES_NOT_FOUND));

        return ClothesDTO.ClothesViewDTO.toDTO(clothes);
    }
}
