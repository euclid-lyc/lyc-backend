package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.enums.Style;
import euclid.lyc_spring.domain.info.InfoStyle;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class SearchResponseDTO {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class MemberPreviewDTO {
        private final Long memberId;
        private final String nickname;
        private final String loginId;
        private final String introduction;
        private final String profileImage;

        public static MemberPreviewDTO toDTO(Member member) {
            return MemberPreviewDTO.builder()
                    .memberId(member.getId())
                    .loginId(member.getLoginId())
                    .introduction(member.getIntroduction())
                    .nickname(member.getNickname())
                    .profileImage(member.getProfileImage())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class MemberPreViewListDTO{
        private final List<MemberPreviewDTO> members;

        public static MemberPreViewListDTO toDTO(List<MemberPreviewDTO> members) {
            return MemberPreViewListDTO.builder().members(members).build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class MemberKeywordPreviewDTO {
        private final Long memberId;
        private final String nickname;
        private final String loginId;
        private final String introduction;
        private final String profileImage;
        private final List<StyleDTO> styles;

        public static MemberKeywordPreviewDTO toDTO(Member member) {

            List<StyleDTO> styleDTOS = toStyleDTOList(member);

            return MemberKeywordPreviewDTO.builder()
                    .memberId(member.getId())
                    .loginId(member.getLoginId())
                    .introduction(member.getIntroduction())
                    .nickname(member.getNickname())
                    .profileImage(member.getProfileImage())
                    .styles(styleDTOS)
                    .build();
        }

        private static List<StyleDTO> toStyleDTOList(Member member){
            List<InfoStyle> styleList = member.getInfo().getInfoStyleList().stream()
                    .filter(InfoStyle::getIsPrefer)
                    .toList();

            List<StyleDTO> styleDTOS = new ArrayList<>();
            for(InfoStyle style : styleList){
                styleDTOS.add(StyleDTO.toDTO(style));
            }

            return styleDTOS;
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class MemberKeywordPreviewListDTO {
        private final List<MemberKeywordPreviewDTO> members;

        public static MemberKeywordPreviewListDTO toDTO(List<MemberKeywordPreviewDTO> members) {
            return MemberKeywordPreviewListDTO.builder().members(members).build();
        }
    }

    @Getter
    private static class StyleDTO{
        private final Style style;

        @Builder(access = AccessLevel.PRIVATE)
        private StyleDTO(Style style){
            this.style = style;
        }

        public static StyleDTO toDTO(InfoStyle infoStyle){
            return StyleDTO.builder()
                    .style(infoStyle.getStyle()).build();
        }
    }
}
