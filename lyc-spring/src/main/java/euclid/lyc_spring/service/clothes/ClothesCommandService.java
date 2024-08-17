package euclid.lyc_spring.service.clothes;

import euclid.lyc_spring.dto.request.ClothesRequestDTO;
import euclid.lyc_spring.dto.response.ClothesDTO;

public interface ClothesCommandService {

    ClothesDTO.ClothesImageResponseDTO createClothesByImage(ClothesRequestDTO.ClothesByImageDTO clothesByImageDTO);
    ClothesDTO.ClothesTextResponseDTO createClothesByText(ClothesRequestDTO.ClothesByTextDTO clothesByTextDTO);
}
