package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.posting.Posting;
import euclid.lyc_spring.dto.response.MemberDTO.*;
import euclid.lyc_spring.dto.response.ImageDTO.*;
import lombok.*;

import java.util.List;

@Getter
public class PostingDTO {

    @Getter
    public static class PostingImageDTO {

        private final Long postingId;
        private final String image;

        @Builder(access = AccessLevel.PRIVATE)
        private PostingImageDTO(Long postingId, String image) {
            this.postingId = postingId;
            this.image = image;
        }

        public static PostingImageDTO toDTO(Posting posting) {
            return PostingImageDTO.builder()
                    .postingId(posting.getId())
                    .image(posting.getImageList().isEmpty() ? "" : posting.getImageList().get(0).getImage())
                    .build();
        }
    }

    @Getter
    public static class PostingImageListDTO {

        private final Long memberId;
        private final List<PostingImageDTO> imageList;

        @Builder
        public PostingImageListDTO(Long memberId, List<PostingImageDTO> imageList) {
            this.memberId = memberId;
            this.imageList = imageList;
        }
    }

    @Getter
    public static class PostingViewDTO {

        private final MemberProfileDTO fromMember;
        private final MemberProfileDTO toMember;
        private final Long writerId;
        private final String nickname;
        private final String loginId;
        private final Long postingId;
        private final Short minTemp;
        private final Short maxTemp;
        private final String content;
        private final List<ImageInfoDTO> imageInfo;

        @Builder(access = AccessLevel.PRIVATE)
        private PostingViewDTO(MemberProfileDTO fromMember, MemberProfileDTO toMember,
                               Long writerId, String nickname, String loginId,
                               Long postingId, Short minTemp, Short maxTemp, String content,
                               List<ImageInfoDTO> imageInfo) {
            this.fromMember = fromMember;
            this.toMember = toMember;
            this.writerId = writerId;
            this.nickname = nickname;
            this.loginId = loginId;
            this.postingId = postingId;
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
            this.content = content;
            this.imageInfo = imageInfo;
        }

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

}
