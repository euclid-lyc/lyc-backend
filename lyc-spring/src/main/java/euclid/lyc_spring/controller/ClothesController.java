package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.request.ClothesRequestDTO;
import euclid.lyc_spring.dto.response.ClothesDTO;
import euclid.lyc_spring.service.clothes.ClothesCommandService;
import euclid.lyc_spring.service.clothes.ClothesQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Clothes", description = "옷장 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc/clothes")
public class ClothesController {

    private final ClothesQueryService clothesQueryService;
    private final ClothesCommandService clothesCommandService;

    @Operation(summary = "옷장 게시글 작성하기(사진)", description = "사진과 함께 옷장 게시글을 작성합니다.")
    @PostMapping("/images")
    ApiResponse<ClothesDTO.ClothesImageResponseDTO> createClothesByImage(
            @RequestBody ClothesRequestDTO.ClothesByImageDTO clothesByImageDTO) {
        ClothesDTO.ClothesImageResponseDTO clothesImageResponseDTO = clothesCommandService.createClothesByImage(clothesByImageDTO);
        return ApiResponse.onSuccess(SuccessStatus._CLOTHES_BY_IMAGE_CREATED, clothesImageResponseDTO);
    }

    @Operation(summary = "옷장 게시글 작성하기(텍스트)", description = "텍스트와 함께 옷장 게시글을 작성합니다.")
    @PostMapping("/texts")
    ApiResponse<ClothesDTO.ClothesTextResponseDTO> createClothesByText(
            @RequestBody ClothesRequestDTO.ClothesByTextDTO clothesByTextDTO) {
        ClothesDTO.ClothesTextResponseDTO clothesTextResponseDTO = clothesCommandService.createClothesByText(clothesByTextDTO);
        return ApiResponse.onSuccess(SuccessStatus._CLOTHES_BY_TEXT_CREATED, clothesTextResponseDTO);
    }

    @Operation(summary = "옷장 게시글 목록 불러오기", description = "옷장 게시글 목록을 불러옵니다.")
    @GetMapping("/members/{memberId}")
    ApiResponse<ClothesDTO.ClothesListDTO> getClothesByMemberId(@PathVariable("memberId") Long memberId) {
        ClothesDTO.ClothesListDTO clothesListDTO = clothesQueryService.getClothesList(memberId);
        return ApiResponse.onSuccess(SuccessStatus._CLOTHES_LIST_FETCHED, clothesListDTO);
    }

    @Operation(summary = "옷장 게시글 불러오기", description = "옷장 게시글을 불러옵니다.")
    @GetMapping("/{clothesId}")
    void getClothes(@PathVariable("clothesId") Long clothesId) {}


}
