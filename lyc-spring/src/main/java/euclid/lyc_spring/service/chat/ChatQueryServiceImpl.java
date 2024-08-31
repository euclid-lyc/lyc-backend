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
    public ChatResponseDTO.ChatPreviewListDTO getAllChats() {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<ChatResponseDTO.ChatPreviewDTO> chatPreviewDTOS = memberChatRepository.findAllByMemberId(member.getId()).stream()
                .map(memberChat ->getChatMemberPreviewDTO(memberChat.getChat()))
                .toList();

        return ChatResponseDTO.ChatPreviewListDTO.toDTO(chatPreviewDTOS);
    }

    @Override
    public ChatResponseDTO.ChatMemberListDTO getChatMembers(Long chatId) {

        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Chat chat = chatRepository.findById(chatId)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        // 채팅에 참여 중인 회원만 대화상대 목록 조회 가능
        if (!memberChatRepository.existsByMemberIdAndChatId(member.getId(), chatId)) {
            throw new ChatHandler(ErrorStatus.CHAT_PARTICIPANTS_ONLY_ALLOWED);
        }

        return ChatResponseDTO.ChatMemberListDTO.toDTO(chat, member);
    }

    private static ChatResponseDTO.ChatPreviewDTO getChatMemberPreviewDTO(Chat chat) {
        Optional<ChatResponseDTO.ChatPreviewDTO> chatPreviewDTO = chat.getMemberChatList().stream()
                .map(memberChat -> getChatMemberPreviewDTO(memberChat, memberChat.getMember()))
                .max(Comparator.comparing(ChatResponseDTO.ChatPreviewDTO::getCreatedAt));

        if (chatPreviewDTO.isEmpty()) {
            throw new ChatHandler(ErrorStatus.CHAT_MESSAGE_NOT_FOUND);
        } else {
            return chatPreviewDTO.get();
        }
    }

    private static ChatResponseDTO.ChatPreviewDTO getChatMemberPreviewDTO(MemberChat memberChat, Member member) {
        Optional<TextMessage> optionalTextMessage = memberChat.getTextMessageList().stream()
                .max(Comparator.comparing(TextMessage::getCreatedAt));
        Optional<ImageMessage> optionalImageMessage = memberChat.getImageMessageList().stream()
                .max(Comparator.comparing(ImageMessage::getCreatedAt));

        if (optionalTextMessage.isEmpty() && optionalImageMessage.isEmpty()) {
            return ChatResponseDTO.ChatPreviewDTO.toDTO(member, memberChat.getChat(), "", LocalDateTime.MIN);
        } else if (optionalTextMessage.isPresent() && optionalImageMessage.isEmpty()) {
            TextMessage textMessage = optionalTextMessage.get();
            return ChatResponseDTO.ChatPreviewDTO.toDTO(member, memberChat.getChat(), textMessage.getContent(), textMessage.getCreatedAt());
        } else if (optionalTextMessage.isEmpty()) {
            ImageMessage imageMessage = optionalImageMessage.get();
            return ChatResponseDTO.ChatPreviewDTO.toDTO(member, memberChat.getChat(), "사진", imageMessage.getCreatedAt());
        } else {
            TextMessage textMessage = optionalTextMessage.get();
            ImageMessage imageMessage = optionalImageMessage.get();
            if (textMessage.getCreatedAt().isAfter(imageMessage.getCreatedAt())) {
                return ChatResponseDTO.ChatPreviewDTO.toDTO(member, memberChat.getChat(), textMessage.getContent(), textMessage.getCreatedAt());
            } else {
                return ChatResponseDTO.ChatPreviewDTO.toDTO(member, memberChat.getChat(), "사진", imageMessage.getCreatedAt());
            }
        }
    }

/*-------------------------------------------------- 메시지 --------------------------------------------------*/
/*-------------------------------------------------- 일정 --------------------------------------------------*/
/*-------------------------------------------------- 사진 및 동영상 --------------------------------------------------*/
}
