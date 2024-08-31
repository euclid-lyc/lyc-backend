package euclid.lyc_spring.service.chat;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ChatHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.ImageMessage;
import euclid.lyc_spring.domain.chat.TextMessage;
import euclid.lyc_spring.domain.mapping.MemberChat;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import euclid.lyc_spring.repository.ChatRepository;
import euclid.lyc_spring.repository.CommissionRepository;
import euclid.lyc_spring.repository.MemberChatRepository;
import euclid.lyc_spring.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryServiceImpl implements ChatQueryService {

    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final CommissionRepository commissionRepository;

    private final MemberChatRepository memberChatRepository;

/*-------------------------------------------------- 채팅방 --------------------------------------------------*/

    @Override
    public ChatResponseDTO.ChatMemberPreviewListDTO getAllChats() {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<ChatResponseDTO.ChatMemberPreviewDTO> chatMemberPreviewDTOS = memberChatRepository.findAllByMemberId(member.getId()).stream()
                .map(memberChat ->getChatMemberPreviewDTO(memberChat.getChat()))
                .toList();

        return ChatResponseDTO.ChatMemberPreviewListDTO.toDTO(chatMemberPreviewDTOS);
    }

    private static ChatResponseDTO.ChatMemberPreviewDTO getChatMemberPreviewDTO(Chat chat) {
        Optional<ChatResponseDTO.ChatMemberPreviewDTO> chatMemberPreviewDTO = chat.getMemberChatList().stream()
                .map(memberChat -> getChatMemberPreviewDTO(memberChat, memberChat.getMember()))
                .max(Comparator.comparing(ChatResponseDTO.ChatMemberPreviewDTO::getCreatedAt));

        if (chatMemberPreviewDTO.isEmpty()) {
            throw new ChatHandler(ErrorStatus.CHAT_MESSAGE_NOT_FOUND);
        } else {
            return chatMemberPreviewDTO.get();
        }
    }

    private static ChatResponseDTO.ChatMemberPreviewDTO getChatMemberPreviewDTO(MemberChat memberChat, Member member) {
        Optional<TextMessage> optionalTextMessage = memberChat.getTextMessageList().stream()
                .max(Comparator.comparing(TextMessage::getCreatedAt));
        Optional<ImageMessage> optionalImageMessage = memberChat.getImageMessageList().stream()
                .max(Comparator.comparing(ImageMessage::getCreatedAt));

        if (optionalTextMessage.isEmpty() && optionalImageMessage.isEmpty()) {
            return ChatResponseDTO.ChatMemberPreviewDTO.toDTO(member, "", LocalDateTime.MIN);
        } else if (optionalTextMessage.isPresent() && optionalImageMessage.isEmpty()) {
            TextMessage textMessage = optionalTextMessage.get();
            return ChatResponseDTO.ChatMemberPreviewDTO.toDTO(member, textMessage.getContent(), textMessage.getCreatedAt());
        } else if (optionalTextMessage.isEmpty()) {
            ImageMessage imageMessage = optionalImageMessage.get();
            return ChatResponseDTO.ChatMemberPreviewDTO.toDTO(member, "사진", imageMessage.getCreatedAt());
        } else {
            TextMessage textMessage = optionalTextMessage.get();
            ImageMessage imageMessage = optionalImageMessage.get();
            if (textMessage.getCreatedAt().isAfter(imageMessage.getCreatedAt())) {
                return ChatResponseDTO.ChatMemberPreviewDTO.toDTO(member, textMessage.getContent(), textMessage.getCreatedAt());
            } else {
                return ChatResponseDTO.ChatMemberPreviewDTO.toDTO(member, "사진", imageMessage.getCreatedAt());
            }
        }
    }

/*-------------------------------------------------- 메시지 --------------------------------------------------*/
/*-------------------------------------------------- 일정 --------------------------------------------------*/
/*-------------------------------------------------- 사진 및 동영상 --------------------------------------------------*/
}
