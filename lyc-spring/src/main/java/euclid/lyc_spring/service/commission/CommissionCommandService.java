package euclid.lyc_spring.service.commission;

import euclid.lyc_spring.dto.request.CommissionRequestDTO;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import euclid.lyc_spring.dto.response.CommissionDTO;

public interface CommissionCommandService {

    CommissionDTO.CommissionViewDTO writeCommission(CommissionRequestDTO.CommissionDTO commissionRequestDTO);
    CommissionDTO.CommissionViewDTO acceptCommission(Long commissionId);
    CommissionDTO.CommissionViewDTO declineCommission(Long commissionId);
    CommissionDTO.CommissionViewDTO updateCommission(Long commissionId, CommissionRequestDTO.CommissionDTO commissionRequestDTO);
    CommissionDTO.CommissionViewDTO requestCommissionTermination(Long chatId);
    CommissionDTO.CommissionViewDTO terminateCommission(Long chatId);
    CommissionDTO.CommissionViewDTO declineCommissionTermination(Long chatId);

    // CommissionClothes
    CommissionDTO.ClothesViewDTO saveCommissionedClothes(Long chatId, CommissionRequestDTO.ClothesDTO clothesRequestDTO);
    CommissionDTO.ClothesViewDTO deleteCommissionedClothes(Long chatId, Long clothesId);
    ChatResponseDTO.ShareClothesListDTO changeCommissionedClothesPublic(Long chatId);
    ChatResponseDTO.ShareClothesListDTO changeCommissionedClothesPrivate(Long chatId);
}
