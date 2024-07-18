package euclid.lyc_spring.service;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.clothes.Clothes;
import euclid.lyc_spring.domain.clothes.ClothesImage;
import euclid.lyc_spring.domain.clothes.ClothesText;
import euclid.lyc_spring.dto.request.ClothesRequestDTO.*;
import euclid.lyc_spring.dto.response.ClothesDTO.*;
import euclid.lyc_spring.repository.ClothesImageRepository;
import euclid.lyc_spring.repository.ClothesRepository;
import euclid.lyc_spring.repository.ClothesTextRepository;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ClothesService {

    private final MemberRepository memberRepository;
    private final ClothesRepository clothesRepository;
    private final ClothesImageRepository clothesImageRepository;
    private final ClothesTextRepository clothesTextRepository;

    /**
     * POST API
     */

    @Transactional
    public ClothesImageResponseDTO createClothesByImage(Long memberId, ClothesByImageDTO clothesByImageDTO) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Clothes clothes = new Clothes(member);

        member.addClothes(clothes);
        clothesRepository.save(clothes);
        createClothesImage(clothes, clothesByImageDTO);

        return ClothesImageResponseDTO.toDTO(clothes);
    }

    private void createClothesImage(Clothes clothes, ClothesByImageDTO clothesByImageDTO) {

        ClothesImage clothesImage = ClothesImage.builder()
                .image(clothesByImageDTO.getImage())
                .text(clothesByImageDTO.getText())
                .build();

        clothes.addClothesImage(clothesImage);
        clothesImageRepository.save(clothesImage);
    }

    @Transactional
    public ClothesTextResponseDTO createClothesByText(Long memberId, ClothesByTextDTO clothesByTextDTO) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Clothes clothes = new Clothes(member);

        member.addClothes(clothes);
        clothesRepository.save(clothes);
        createClothesByText(clothes, clothesByTextDTO);

        return ClothesTextResponseDTO.toDTO(clothes);
    }

    private void createClothesByText(Clothes clothes, ClothesByTextDTO clothesByTextDTO) {

        ClothesText clothesText = ClothesText.builder()
                .name(clothesByTextDTO.getName())
                .material(clothesByTextDTO.getMaterial())
                .fit(clothesByTextDTO.getFit())
                .build();

        clothes.addClothesText(clothesText);
        clothesTextRepository.save(clothesText);
    }
}
