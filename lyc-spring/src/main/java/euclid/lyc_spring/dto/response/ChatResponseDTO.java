package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class ChatResponseDTO {

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ChatMemberPreviewListDTO {
        private final List<ChatMemberPreviewDTO> chats;

        public static ChatMemberPreviewListDTO toDTO(List<ChatMemberPreviewDTO> chats) {
            return ChatMemberPreviewListDTO.builder().chats(chats).build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ChatMemberPreviewDTO {

        private final String nickname;
        private final String loginId;
        private final String profileImage;
        private final String recentMessage;
        private final LocalDateTime createdAt;

        public static ChatMemberPreviewDTO toDTO(Member member, String recentMessage, LocalDateTime createdAt) {
            return ChatMemberPreviewDTO.builder()
                    .nickname(member.getNickname())
                    .loginId(member.getLoginId())
                    .profileImage(member.getProfileImage())
                    .recentMessage(recentMessage)
                    .createdAt(createdAt)
                    .build();
        }
    }
}
