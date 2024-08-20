package euclid.lyc_spring.service.clothes;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.clothes.Clothes;
import euclid.lyc_spring.domain.clothes.ClothesImage;
import euclid.lyc_spring.domain.clothes.ClothesText;
import euclid.lyc_spring.dto.request.ClothesRequestDTO;
import euclid.lyc_spring.dto.response.ClothesDTO;
import euclid.lyc_spring.repository.ClothesImageRepository;
import euclid.lyc_spring.repository.ClothesRepository;
import euclid.lyc_spring.repository.ClothesTextRepository;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClothesCommandServiceImpl implements ClothesCommandService {

    private final MemberRepository memberRepository;
    private final ClothesRepository clothesRepository;
    private final ClothesImageRepository clothesImageRepository;
    private final ClothesTextRepository clothesTextRepository;

    @Override
    public ClothesDTO.ClothesImageResponseDTO createClothesByImage(ClothesRequestDTO.ClothesByImageDTO clothesByImageDTO) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Clothes clothes = new Clothes(member, clothesByImageDTO.getTitle(), clothesByImageDTO.getText(), false);

        member.addClothes(clothes);
        clothes = clothesRepository.save(clothes);
        createClothesImage(clothes, clothesByImageDTO);

        return ClothesDTO.ClothesImageResponseDTO.toDTO(clothes);
    }

    private void createClothesImage(Clothes clothes, ClothesRequestDTO.ClothesByImageDTO clothesByImageDTO) {

        ClothesImage clothesImage = ClothesImage.builder()
                .image(clothesByImageDTO.getImage())
                .build();

        clothes.addClothesImage(clothesImage);
        clothesImageRepository.save(clothesImage);
    }

    public ClothesDTO.ClothesTextResponseDTO createClothesByText(ClothesRequestDTO.ClothesByTextDTO clothesByTextDTO) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Clothes clothes = new Clothes(member, clothesByTextDTO.getTitle(), clothesByTextDTO.getText(), true);

        member.addClothes(clothes);
        clothesRepository.save(clothes);
        createClothesByText(clothes, clothesByTextDTO);

        return ClothesDTO.ClothesTextResponseDTO.toDTO(clothes);
    }

    private void createClothesByText(Clothes clothes, ClothesRequestDTO.ClothesByTextDTO clothesByTextDTO) {

        ClothesText clothesText = ClothesText.builder()
                .material(clothesByTextDTO.getMaterial())
                .fit(clothesByTextDTO.getFit())
                .build();

        clothes.addClothesText(clothesText);
        clothesTextRepository.save(clothesText);
    }
}
