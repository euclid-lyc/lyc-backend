package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.mapping.MemberChat;
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
    public static class ChatPreviewListDTO {
        private final List<ChatPreviewDTO> chats;

        public static ChatPreviewListDTO toDTO(List<ChatPreviewDTO> chats) {
            return ChatPreviewListDTO.builder().chats(chats).build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ChatPreviewDTO {

        private final Long chatId;
        private final String nickname;
        private final String loginId;
        private final String profileImage;
        private final String recentMessage;
        private final LocalDateTime createdAt;

        public static ChatPreviewDTO toDTO(Member member, Chat chat, String recentMessage, LocalDateTime createdAt) {
            return ChatPreviewDTO.builder()
                    .chatId(chat.getId())
                    .nickname(member.getNickname())
                    .loginId(member.getLoginId())
                    .profileImage(member.getProfileImage())
                    .recentMessage(recentMessage)
                    .createdAt(createdAt)
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ChatMemberListDTO {

        private final List<ChatMemberDTO> members;

        public static ChatMemberListDTO toDTO(Chat chat, Member member) {
            return ChatMemberListDTO.builder()
                    .members(chat.getMemberChatList().stream()
                            .map(memberChat -> ChatMemberDTO.toDTO(memberChat, member))
                            .toList())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ChatMemberDTO {

        private final String nickname;
        private final String profileImage;
        private final Boolean isMine;

        public static ChatMemberDTO toDTO(MemberChat memberChat, Member member) {
            return ChatMemberDTO.builder()
                    .nickname(memberChat.getMember().getNickname())
                    .profileImage(memberChat.getMember().getProfileImage())
                    .isMine(memberChat.getMember().equals(member))
                    .build();
        }
    }
}