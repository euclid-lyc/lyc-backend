package euclid.lyc_spring.service.clothes;

import euclid.lyc_spring.dto.response.ClothesDTO;

public interface ClothesQueryService {

    ClothesDTO.ClothesListDTO getClothesList(Long memberId);
    ClothesDTO.ClothesViewDTO getClothes(Long clothesId);
}
