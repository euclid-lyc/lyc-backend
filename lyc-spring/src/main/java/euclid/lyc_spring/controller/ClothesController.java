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

    @Operation(summary = "옷장 게시글 작성하기(사진)", description = "사진과 함께 옷장 게시글을 작성합니다.")
    @PostMapping("/members/{memberId}/clothes-by-image")
    ApiResponse<ClothesImageResponseDTO> createClothesByImage(@PathVariable("memberId") Long memberId,
                                                                         @RequestBody ClothesByImageDTO clothesByImageDTO) {
        ClothesImageResponseDTO clothesImageResponseDTO = clothesService.createClothesByImage(memberId, clothesByImageDTO);
        return ApiResponse.onSuccess(clothesImageResponseDTO);
    }

    @Operation(summary = "옷장 게시글 작성하기(텍스트)", description = "텍스트와 함께 옷장 게시글을 작성합니다.")
    @PostMapping("/members/{memberId}/clothes-by-text")
    ApiResponse<ClothesTextResponseDTO> createClothesByText(@PathVariable("memberId") Long memberId,
                                                              @RequestBody ClothesByTextDTO clothesByTextDTO) {
        ClothesTextResponseDTO clothesTextResponseDTO = clothesService.createClothesByText(memberId, clothesByTextDTO);
        return ApiResponse.onSuccess(clothesTextResponseDTO);
    }

}
