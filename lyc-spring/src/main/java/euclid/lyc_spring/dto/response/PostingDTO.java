package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.dto.response.MemberDTO.*;
import euclid.lyc_spring.dto.response.ImageDTO.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostingDTO {

    @Getter
    @Builder
    public static class PostingImageListDTO {

        private final List<PostingImageDTO> imageList;

        public static PostingImageListDTO toDTO(List<PostingImageDTO> imageList) {
            return PostingImageListDTO.builder().imageList(imageList).build();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class RecentPostingListDTO {
        private final List<PostingImageDTO> posting;
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class PostingImageDTO {

        private final Long postingId;
        private final String image;
        private final LocalDateTime createdAt;

        public static PostingImageDTO toDTO(Posting posting) {
            return PostingDTO.PostingImageDTO.builder()
                    .postingId(posting.getId())
                    .image(posting.getImageList().isEmpty() ? "" : posting.getImageList().get(0).getImage())
                    .createdAt(posting.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class PostingViewDTO {

        private final MemberProfileDTO fromMember;
        private final MemberProfileDTO toMember;
        private final Long writerId;
        private final String nickname;
        private final String loginId;
        private final Long postingId;
        private final Double minTemp;
        private final Double maxTemp;
        private final String content;
        private final List<ImageInfoDTO> imageInfo;
        private final Long likes;

        public static PostingViewDTO toDTO(Posting posting) {
            return PostingViewDTO.builder()
                    .fromMember(MemberProfileDTO.toDTO(posting.getFromMember()))
                    .toMember(MemberProfileDTO.toDTO(posting.getToMember()))
                    .writerId(posting.getWriter().getId())
                    .nickname(posting.getWriter().getNickname())
                    .loginId(posting.getWriter().getLoginId())
                    .postingId(posting.getId())
                    .minTemp(posting.getMinTemp())
                    .maxTemp(posting.getMaxTemp())
                    .content(posting.getContent())
                    .likes(posting.getLikes())
                    .imageInfo(posting.getImageList().stream()
                            .map(ImageInfoDTO::toDTO)
                            .toList())
                    .build();
        }
    }

    @Getter
    public static class ClickDTO {

        private final Long memberId;
        private final Long postingId;
        private final Boolean isClicked;

        @Builder
        public ClickDTO(Long memberId, Long postingId, Boolean isClicked) {
            this.memberId = memberId;
            this.postingId = postingId;
            this.isClicked = isClicked;
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class RecommendedPostingListDTO {

        private final List<RecommendedPostingDTO> postings;

        public static RecommendedPostingListDTO toDTO(List<RecommendedPostingDTO> postings) {
            return RecommendedPostingListDTO.builder()
                    .postings(postings)
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class RecommendedPostingDTO {

        private final Long postingId;
        private final String image;
        private final LocalDateTime createdAt;
        private final Long totalScore;

        public static RecommendedPostingDTO toDTO(Posting posting, Long totalScore) {
            return RecommendedPostingDTO.builder()
                    .postingId(posting.getId())
                    .image(posting.getImageList().isEmpty() ? "" : posting.getImageList().get(0).getImage())
                    .createdAt(posting.getCreatedAt())
                    .totalScore(totalScore)
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor
    public static class PostingIdDTO {

        private final Long postingId;
    }

    @Getter
    @RequiredArgsConstructor
    public static class SavedPostingIdDTO {

        private final Long postingId;
        private final Long savedPostingId;
    }

    @Getter
    @RequiredArgsConstructor
    public static class PostingScoreDTO {
        private final Long memberId;
        private final Long postingId;
        private final Long totalScore;
    }

}