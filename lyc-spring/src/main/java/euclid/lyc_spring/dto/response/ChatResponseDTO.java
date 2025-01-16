package euclid.lyc_spring.dto.response;

import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.Message;
import euclid.lyc_spring.domain.chat.Schedule;
import euclid.lyc_spring.domain.enums.MessageCategory;
import euclid.lyc_spring.domain.mapping.MemberChat;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
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
    @RequiredArgsConstructor
    @Builder
    public static class ChatPreviewDTO {

        private final Long chatId;
        private final String nickname;
        private final String loginId;
        private final String profileImage;
        private final Boolean isText;
        private final String content;
        private final LocalDateTime createdAt;

    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class LatestMessageDTO {

        private final Long chatId;
        private final LocalDateTime createdAt;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class MemberPreviewDTO {
        private final String nickname;
        private final String loginId;
        private final String profileImage;
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class MessageInfoDTO {

        private final String sender;
        private final String profileImage;
        private final String content;
        private final MessageCategory msgType;
        private final Boolean isText;
        private final Boolean isChecked;
        private final LocalDateTime createdAt;

        public static MessageInfoDTO toDTO(Message message) {
            return MessageInfoDTO.builder()
                    .sender(message.getMemberChat().getMember().getNickname())
                    .profileImage(message.getMemberChat().getMember().getProfileImage())
                    .content(message.getContent())
                    .msgType(message.getCategory())
                    .isText(message.getIsText())
                    .isChecked(message.getIsChecked())
                    .createdAt(message.getCreatedAt())
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
        private final Boolean isDirector;

        public static ChatMemberDTO toDTO(MemberChat memberChat, Member member) {
            return ChatMemberDTO.builder()
                    .nickname(memberChat.getMember().getNickname())
                    .profileImage(memberChat.getMember().getProfileImage())
                    .isMine(memberChat.getMember().equals(member))
                    .isDirector(memberChat.getChat().getCommission().getDirector()
                            .equals(memberChat.getMember()))
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class ChatInactiveDTO {
        private final LocalDateTime inactive;
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ScheduleListDTO {

        private final List<ScheduleDTO> schedules;

        public static ScheduleListDTO toDTO(List<Schedule> schedules) {
            return ScheduleListDTO.builder()
                    .schedules(schedules.stream()
                            .map(ScheduleDTO::toDTO)
                            .toList())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ScheduleDTO {

        private final LocalDate date;
        private final String memo;

        public static ScheduleDTO toDTO(Schedule schedule) {
            return ScheduleDTO.builder()
                    .date(schedule.getDate())
                    .memo(schedule.getMemo())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ImageListDTO {

        private final List<ChatImageDTO> images;

        public static ImageListDTO toDTO(List<Message> imageMessages) {
            return ImageListDTO.builder()
                    .images(imageMessages.stream()
                            .map(ChatImageDTO::toDTO)
                            .toList())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ChatImageDTO {

        private final Long imageId;
        private final String imageUrl;
        private final LocalDateTime createdAt;

        public static ChatImageDTO toDTO(Message imageMessage) {
            return ChatImageDTO.builder()
                    .imageId(imageMessage.getId())
                    .imageUrl(imageMessage.getContent())
                    .createdAt(imageMessage.getCreatedAt())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ChatDTO {
        private final Long commissionId;
        private final List<ChatMemberDTO> chatMembers;
        private final List<MessageInfoDTO> messages;
        public static ChatDTO toDTO(List<Message> messages, Chat chat, Member member) {
            return ChatDTO.builder()
                    .commissionId(chat.getCommission().getId())
                    .chatMembers(chat.getMemberChatList().stream()
                            .map(memberChat -> ChatMemberDTO.toDTO(memberChat, member))
                            .toList())
                    .messages(messages.stream()
                            .map(MessageInfoDTO::toDTO)
                            .toList())
                    .build();
        }
    }

    @Getter
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    @Builder(access = AccessLevel.PRIVATE)
    public static class ShareClothesListDTO{
        private final Long chatId;
        private final Boolean isShared;
        public static ShareClothesListDTO toDTO(Chat chat) {
            return ShareClothesListDTO.builder()
                    .chatId(chat.getId())
                    .isShared(chat.getIsShared())
                    .build();
        }
    }

}