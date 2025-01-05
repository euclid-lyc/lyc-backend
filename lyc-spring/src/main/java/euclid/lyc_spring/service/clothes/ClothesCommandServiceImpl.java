package euclid.lyc_spring.service.clothes;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ClothesHandler;
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
import euclid.lyc_spring.service.s3.S3ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ClothesCommandServiceImpl implements ClothesCommandService {

    private final MemberRepository memberRepository;
    private final ClothesRepository clothesRepository;
    private final ClothesImageRepository clothesImageRepository;
    private final ClothesTextRepository clothesTextRepository;

    private final S3ImageService s3ImageService;

    @Override
    public ClothesDTO.ClothesImageResponseDTO createClothesByImage(ClothesRequestDTO.ClothesByImageDTO clothesByImageDTO, String imageUrl) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Clothes clothes = Clothes.builder()
                .member(member)
                .title(clothesByImageDTO.getTitle())
                .text(clothesByImageDTO.getText())
                .isText(false)
                .build();

        clothes = clothesRepository.save(clothes);

        createClothesImage(clothes, imageUrl);
        member.addClothes(clothes);

        return ClothesDTO.ClothesImageResponseDTO.toDTO(clothes);
    }

    private void createClothesImage(Clothes clothes, String imageUrl) {

        ClothesImage clothesImage = ClothesImage.builder()
                .image(imageUrl)
                .clothes(clothes)
                .build();

        clothes.setClothesImage(clothesImage);
        clothesImageRepository.save(clothesImage);
    }

    public ClothesDTO.ClothesTextResponseDTO createClothesByText(ClothesRequestDTO.ClothesByTextDTO clothesByTextDTO) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Clothes clothes = Clothes.builder()
                .member(member)
                .title(clothesByTextDTO.getTitle())
                .text(clothesByTextDTO.getText())
                .isText(true)
                .build();

        clothes = clothesRepository.save(clothes);

        createClothesByText(clothes, clothesByTextDTO);
        member.addClothes(clothes);

        return ClothesDTO.ClothesTextResponseDTO.toDTO(clothes);
    }

    private void createClothesByText(Clothes clothes, ClothesRequestDTO.ClothesByTextDTO clothesByTextDTO) {

        ClothesText clothesText = ClothesText.builder()
                .material(clothesByTextDTO.getMaterial())
                .fit(clothesByTextDTO.getFit())
                .clothes(clothes)
                .build();

        clothes.setClothesText(clothesText);
        clothesTextRepository.save(clothesText);
    }

    @Override
    public ClothesDTO.ClothesPreviewDTO deleteClothes(Long clothesId) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Clothes clothes = clothesRepository.findById(clothesId)
                .orElseThrow(() -> new ClothesHandler(ErrorStatus.CLOTHES_NOT_FOUND));

        // 로그인한 회원이 옷장 게시글 작성자인지 확인
        if (!member.equals(clothes.getMember())) {
            throw new ClothesHandler(ErrorStatus.CLOTHES_WRITER_NOT_FOUND);
        }

        Optional<ClothesImage> clothesImage = clothesImageRepository.findByClothesId(clothesId);
        Optional<ClothesText> clothesText = clothesTextRepository.findByClothesId(clothesId);

        clothesImage.ifPresent(image -> {
            s3ImageService.deleteImageFromS3(image.getImage());
            clothesImageRepository.deleteById(image.getId());
        });
        clothesText.ifPresent(text -> clothesImageRepository.deleteById(text.getId()));

        clothesRepository.deleteById(clothesId);

        return ClothesDTO.ClothesPreviewDTO.toDTO(clothes);
    }
}
