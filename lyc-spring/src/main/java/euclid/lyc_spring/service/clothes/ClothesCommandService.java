package euclid.lyc_spring.service.clothes;

import euclid.lyc_spring.dto.request.ClothesRequestDTO;
import euclid.lyc_spring.dto.response.ClothesDTO;

public interface ClothesCommandService {

    ClothesDTO.ClothesImageResponseDTO createClothesByImage(ClothesRequestDTO.ClothesByImageDTO clothesByImageDTO, String imageUrl);
    ClothesDTO.ClothesTextResponseDTO createClothesByText(ClothesRequestDTO.ClothesByTextDTO clothesByTextDTO);
    ClothesDTO.ClothesPreviewDTO deleteClothes(Long clothesId);
}
