package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.dto.request.ClothesRequestDTO.*;
import euclid.lyc_spring.dto.response.ClothesDTO.*;
import euclid.lyc_spring.service.ClothesService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clothes", description = "옷장 기능 관련 API")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class ClothesController {

    private final ClothesService clothesService;

    /**
     * POST API
     */

    @Operation(summary = "", description = "")
    @PostMapping("/members/{memberId}/clothes-by-image")
    ApiResponse<ClothesImageResponseDTO> createClothesByImage(@PathVariable("memberId") Long memberId,
                                                                         @RequestBody ClothesByImageDTO clothesByImageDTO) {
        ClothesImageResponseDTO clothesImageResponseDTO = clothesService.createClothesByImage(memberId, clothesByImageDTO);
        return ApiResponse.onSuccess(clothesImageResponseDTO);
    }

}
