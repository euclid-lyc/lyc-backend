package euclid.lyc_spring.service;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ClothesHandler;
import euclid.lyc_spring.apiPayload.exception.handler.JwtHandler;
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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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
    public ClothesImageResponseDTO createClothesByImage(java.lang.Long memberId, ClothesByImageDTO clothesByImageDTO) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        authorizeWriter(member);

        Clothes clothes = new Clothes(member, clothesByImageDTO.getTitle(), clothesByImageDTO.getText());

        member.addClothes(clothes);
        clothesRepository.save(clothes);
        createClothesImage(clothes, clothesByImageDTO);

        return ClothesImageResponseDTO.toDTO(clothes);
    }

    private void createClothesImage(Clothes clothes, ClothesByImageDTO clothesByImageDTO) {

        ClothesImage clothesImage = ClothesImage.builder()
                .image(clothesByImageDTO.getImage())
                .build();

        clothes.addClothesImage(clothesImage);
        clothesImageRepository.save(clothesImage);
    }

    @Transactional
    public ClothesTextResponseDTO createClothesByText( Long memberId, ClothesByTextDTO clothesByTextDTO) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        authorizeWriter(member);

        Clothes clothes = new Clothes(member, clothesByTextDTO.getTitle(), clothesByTextDTO.getText());

        member.addClothes(clothes);
        clothesRepository.save(clothes);
        createClothesByText(clothes, clothesByTextDTO);

        return ClothesTextResponseDTO.toDTO(clothes);
    }

    private void createClothesByText(Clothes clothes, ClothesByTextDTO clothesByTextDTO) {

        ClothesText clothesText = ClothesText.builder()
                .material(clothesByTextDTO.getMaterial())
                .fit(clothesByTextDTO.getFit())
                .build();

        clothes.addClothesText(clothesText);
        clothesTextRepository.save(clothesText);
    }

    @Transactional
    public ClothesListDTO getClothesList(java.lang.Long memberId) {

        // Authorization
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN);
        }

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member member = memberRepository.findById(memberId).get();

        List<ClothesInfoDTO> clothesInfoDTOList = clothesRepository.findByMember(member).stream()
                .map(ClothesInfoDTO::toDTO)
                .toList();

        return ClothesListDTO.builder()
                .memberId(memberId)
                .clothesList(clothesInfoDTOList)
                .build();
    }

    @Transactional
    public ClothesViewDTO getClothes(java.lang.Long memberId, java.lang.Long clothesId) {
        // Authorization
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN);
        }

        memberRepository.findById(memberId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Member member = memberRepository.findById(memberId).get();

        Clothes clothes = clothesRepository.findByIdAndMember(clothesId, member)
                .orElseThrow(() -> new ClothesHandler(ErrorStatus.CLOTHES_NOT_FOUND));

        return ClothesViewDTO.toDTO(clothes);
    }

/* ---------------------------------------- 인증/인가 ---------------------------------------- */

    private void authorizeWriter(Member member) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            // 인증 정보가 존재하면 loginId 확인
            String loginId = (String) authentication.getPrincipal();
            if (!loginId.equals(member.getLoginId())) {
                // 글쓴이 혹은 게시글을 저장한 회원과 로그인한 회원이 일치하지 않으면 오류
                throw new JwtHandler(ErrorStatus.JWT_UNAUTHORIZED);
            }
        } else {
            throw new JwtHandler(ErrorStatus.JWT_INVALID_TOKEN);
        }
    }
}
