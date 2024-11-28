package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.service.collage.CollageCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Tag(name = "Collage", description = "콜라주 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc/collages")
public class CollageController {

    private final CollageCommandService collageCommandService;

    @Operation(summary = "[구현완료] 배경 제거 이미지 생성하기", description = """
           ### 입력받은 이미지들의 배경을 제거한 후 반환합니다.
           (누끼 따는 API)
           """)
    @PostMapping(value = "/bg-removal", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<List<String>> createBGRemovalImages(
            @RequestPart List<MultipartFile> multipartFiles
    ) {
        List<String> bgRemovalImageDTOS = collageCommandService.createBGRemovalImages(multipartFiles);
        return ApiResponse.onSuccess(SuccessStatus._COLLAGE_IMAGE_BG_REMOVED, bgRemovalImageDTOS);
    }

    @Operation(summary = "[구현중] 콜라주 생성하기", description = """
            """)
    @PostMapping("/")
    public void createCollage() {}

}
