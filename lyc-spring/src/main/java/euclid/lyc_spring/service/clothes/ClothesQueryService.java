package euclid.lyc_spring.service.clothes;

import euclid.lyc_spring.dto.response.ClothesDTO;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public interface ClothesQueryService {

    ClothesDTO.ClothesListDTO getClothesList(Long memberId, Integer pageSize, LocalDateTime cursorDateTime);
    ClothesDTO.ClothesViewDTO getClothes(Long clothesId);
    boolean getIsText(Long clothesId);
    ClothesDTO.ClothesWithTextDTO getClothesUploadedWithText(Long clothesId);
    ClothesDTO.ClothesWithImageDTO getClothesUploadedWithImage(Long clothesId);
}
