package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.dto.response.PostingDTO.*;
import euclid.lyc_spring.service.PostingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "Post", description = "게시글 기능 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostingController {

    private final PostingService postingService;

    @Operation(summary = "유저의 코디 목록 불러오기", description = "마이페이지에 유저의 코디 목록을 불러옵니다.")
    @GetMapping("/members/{memberId}/coordies")
    ApiResponse<PostingImageListDTO> getMemberCoordies(@PathVariable("memberId") Long memberId) {
        PostingImageListDTO postingImageListDTO = postingService.getMemberCoordies(memberId);
        return ApiResponse.onSuccess(postingImageListDTO);
    }

    @Operation(summary = "유저의 리뷰 목록 불러오기", description = "마이페이지에 유저의 리뷰 목록을 불러옵니다.")
    @GetMapping("/members/{memberId}/reviews")
    ApiResponse<PostingImageListDTO> getMemberReviews(@PathVariable("memberId") Long memberId) {
        PostingImageListDTO postingImageListDTO = postingService.getMemberReviews(memberId);
        return ApiResponse.onSuccess(postingImageListDTO);
    }

}
