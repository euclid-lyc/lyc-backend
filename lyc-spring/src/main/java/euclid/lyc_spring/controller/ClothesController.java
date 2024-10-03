package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.request.ClothesRequestDTO;
import euclid.lyc_spring.dto.response.ClothesDTO;
import euclid.lyc_spring.service.clothes.ClothesCommandService;
import euclid.lyc_spring.service.clothes.ClothesQueryService;
import euclid.lyc_spring.service.s3.S3ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Tag(name = "Clothes", description = "옷장 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc")
public class ClothesController {

    private final ClothesQueryService clothesQueryService;
    private final ClothesCommandService clothesCommandService;
    private final S3ImageService s3ImageService;

    @Operation(summary = "[구현완료] 옷장 게시글 작성하기(사진)", description = """
        사진과 함께 옷장 게시글을 작성합니다.
        
        이미지 요청 형식은 'multipart/form-data', 나머지 데이터의 요청 형식은 'application/json'입니다.
        """)
    @PostMapping(value = "/clothes/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ApiResponse<ClothesDTO.ClothesImageResponseDTO> createClothesByImage(
            @RequestPart ClothesRequestDTO.ClothesByImageDTO clothesByImageDTO,
            @RequestPart(required = false) MultipartFile image) {
        String imageUrl = s3ImageService.upload(image);
        ClothesDTO.ClothesImageResponseDTO clothesImageResponseDTO = clothesCommandService.createClothesByImage(clothesByImageDTO, imageUrl);
        return ApiResponse.onSuccess(SuccessStatus._CLOTHES_BY_IMAGE_CREATED, clothesImageResponseDTO);
    }

    @Operation(summary = "[구현완료] 옷장 게시글 작성하기(텍스트)", description = "텍스트와 함께 옷장 게시글을 작성합니다.")
    @PostMapping("/clothes/texts")
    ApiResponse<ClothesDTO.ClothesTextResponseDTO> createClothesByText(
            @RequestBody ClothesRequestDTO.ClothesByTextDTO clothesByTextDTO) {
        ClothesDTO.ClothesTextResponseDTO clothesTextResponseDTO = clothesCommandService.createClothesByText(clothesByTextDTO);
        return ApiResponse.onSuccess(SuccessStatus._CLOTHES_BY_TEXT_CREATED, clothesTextResponseDTO);
    }

    @Operation(summary = "[구현완료] 옷장 게시글 삭제하기", description = "옷장 게시글을 삭제합니다(이미지 & 텍스트 공통).")
    @DeleteMapping("/clothes/{clothesId}")
    ApiResponse<ClothesDTO.ClothesPreviewDTO> deleteClothes(@PathVariable Long clothesId) {
        ClothesDTO.ClothesPreviewDTO clothesPreviewDTO = clothesCommandService.deleteClothes(clothesId);
        return ApiResponse.onSuccess(SuccessStatus._CLOTHES_DELETED, clothesPreviewDTO);
    }

    @Operation(summary = "[구현완료] 옷장 게시글 목록 불러오기", description = """
            옷장 게시글 목록을 불러옵니다.
            
            커서 기반 페이징이 적용됩니다. cursorDateTime은 이전에 전달된 마지막 옷장 게시글의 업로드 시각입니다.
            
            이 API는 cursorDateTime보다 이전에 업로드된 옷장 게시글의 목록을 불러옵니다.
            """)
    @GetMapping("/clothes/members/{memberId}")
    ApiResponse<ClothesDTO.ClothesListDTO> getClothesByMemberId(
            @PathVariable("memberId") Long memberId,
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam LocalDateTime cursorDateTime) {
        ClothesDTO.ClothesListDTO clothesListDTO = clothesQueryService.getClothesList(memberId, pageSize, cursorDateTime);
        return ApiResponse.onSuccess(SuccessStatus._CLOTHES_LIST_FETCHED, clothesListDTO);
    }

    @Operation(summary = "[구현중] 옷장 게시글 불러오기", description = "옷장 게시글을 불러옵니다.")
    @GetMapping("/clothes/{clothesId}")
    ApiResponse<?> getClothes(@PathVariable("clothesId") Long clothesId) {
        boolean isText = clothesQueryService.getIsText(clothesId);
        if (isText) {
            ClothesDTO.ClothesWithTextDTO clothesWithTextDTO = clothesQueryService.getClothesUploadedWithText(clothesId);
            return ApiResponse.onSuccess(SuccessStatus._CLOTHES_BY_IMAGE_FOUND, clothesWithTextDTO);
        } else {
            ClothesDTO.ClothesWithImageDTO clothesWithImageDTO = clothesQueryService.getClothesUploadedWithImage(clothesId);
            return ApiResponse.onSuccess(SuccessStatus._CLOTHES_BY_TEXT_FOUND, clothesWithImageDTO);
        }
    }


}
