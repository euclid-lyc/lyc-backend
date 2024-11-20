
package euclid.lyc_spring.service.chat;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ChatHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.Message;
import euclid.lyc_spring.domain.chat.Schedule;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import euclid.lyc_spring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatQueryServiceImpl implements ChatQueryService {

    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final MessageRepository messageRepository;

    private final MemberChatRepository memberChatRepository;


    /*-------------------------------------------------- 채팅방 --------------------------------------------------*/

    @Override
    public ChatResponseDTO.ChatPreviewListDTO getAllChats(PageRequest pageRequest) {

        // Authorization
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));

        List<ChatResponseDTO.ChatPreviewDTO> chatPreviewDTOS = memberChatRepository
                .findAllChatsByMemberId(member.getId(), pageRequest).stream()
                .map(latestMessageDTO -> {
                    List<ChatResponseDTO.MemberPreviewDTO> memberPreviewDTOs = memberChatRepository.findPartnerByChatAndMemberId(latestMessageDTO.getChatId(), member.getId());
                    if (memberPreviewDTOs.isEmpty()) {
                        throw new ChatHandler(ErrorStatus.CHAT_PARTNER_NOT_FOUND);
                    } else {
                        Message message = messageRepository.findByMemberChatChatIdAndCreatedAt(latestMessageDTO.getChatId(), latestMessageDTO.getCreatedAt())
                                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_MESSAGE_NOT_FOUND));
                        return ChatResponseDTO.ChatPreviewDTO.builder()
                                .chatId(latestMessageDTO.getChatId())
                                .nickname(memberPreviewDTOs.get(0).getNickname())
                                .loginId(memberPreviewDTOs.get(0).getLoginId())
                                .profileImage(memberPreviewDTOs.get(0).getProfileImage())
                                .isText(message.getIsText())
                                .content(message.getContent())
                                .createdAt(message.getCreatedAt())
                                .build();
                    }
                })
                .sorted(Comparator.comparing(ChatResponseDTO.ChatPreviewDTO::getCreatedAt).reversed())
                .toList();

        return ChatResponseDTO.ChatPreviewListDTO.toDTO(chatPreviewDTOS);
    }

    @Override
    public ChatResponseDTO.ChatMemberListDTO getChatMembers(Long chatId) {

        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Chat chat = chatRepository.findByIdAndInactive(chatId, null)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        // 채팅에 참여 중인 회원만 대화상대 목록 조회 가능
        if (!memberChatRepository.existsByMemberIdAndChatId(member.getId(), chatId)) {
            throw new ChatHandler(ErrorStatus.CHAT_PARTICIPANTS_ONLY_ALLOWED);
        }

        return ChatResponseDTO.ChatMemberListDTO.toDTO(chat, member);
    }

    /*-------------------------------------------------- 메시지 --------------------------------------------------*/
    /*-------------------------------------------------- 일정 --------------------------------------------------*/

    @Override
    public ChatResponseDTO.ScheduleListDTO getSchedules(Long chatId, Integer year, Integer month) {

        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Chat chat = chatRepository.findByIdAndInactive(chatId, null)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        // 채팅에 참여 중인 회원만 일정 목록 조회 가능
        if (!memberChatRepository.existsByMemberIdAndChatId(member.getId(), chatId)) {
            throw new ChatHandler(ErrorStatus.CHAT_PARTICIPANTS_ONLY_ALLOWED);
        }

        List<Schedule> schedules = chat.getScheduleList().stream()
                .filter(schedule -> schedule.getDate().getYear() == year && schedule.getDate().getMonthValue() == month)
                .sorted(Comparator.comparing(Schedule::getDate))
                .toList();

        return ChatResponseDTO.ScheduleListDTO.toDTO(schedules);
    }

    @Override
    public ChatResponseDTO.ScheduleListDTO getSchedules(Long chatId, Integer year, Integer month, Integer day) {
        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Chat chat = chatRepository.findByIdAndInactive(chatId, null)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        // 채팅에 참여 중인 회원만 일정 목록 조회 가능
        if (!memberChatRepository.existsByMemberIdAndChatId(member.getId(), chatId)) {
            throw new ChatHandler(ErrorStatus.CHAT_PARTICIPANTS_ONLY_ALLOWED);
        }

        List<Schedule> schedules = chat.getScheduleList().stream()
                .filter(schedule -> schedule.getDate().getYear() == year &&
                        schedule.getDate().getMonthValue() == month &&
                        schedule.getDate().getDayOfMonth() == day)
                .sorted(Comparator.comparing(Schedule::getDate))
                .toList();

        return ChatResponseDTO.ScheduleListDTO.toDTO(schedules);
    }

    /*-------------------------------------------------- 사진 및 동영상 --------------------------------------------------*/

    @Override
    public ChatResponseDTO.ImageListDTO getAllChatImages(Long chatId, Integer pageSize, LocalDateTime cursorDateTime) {

        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Chat chat = chatRepository.findByIdAndInactive(chatId, null)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        // 채팅에 참여 중인 회원만 사진 목록 조회 가능
        if (!memberChatRepository.existsByMemberIdAndChatId(member.getId(), chatId)) {
            throw new ChatHandler(ErrorStatus.CHAT_PARTICIPANTS_ONLY_ALLOWED);
        }

        List<Message> imageMessages = memberChatRepository
                .findImageMessagesSortedByCreatedAt(chatId, pageSize, cursorDateTime);

        return ChatResponseDTO.ImageListDTO.toDTO(imageMessages);
    }

    @Override
    public ChatResponseDTO.ChatImageDTO getChatImage(Long chatId, Long imageId) {

        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        chatRepository.findByIdAndInactive(chatId, null)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        // 채팅에 참여 중인 회원만 사진 조회 가능
        if (!memberChatRepository.existsByMemberIdAndChatId(member.getId(), chatId)) {
            throw new ChatHandler(ErrorStatus.CHAT_PARTICIPANTS_ONLY_ALLOWED);
        }

        Message imageMessage = messageRepository.findByIdAndIsText(imageId, false)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_IMAGE_NOT_FOUND));

        return ChatResponseDTO.ChatImageDTO.toDTO(imageMessage);
    }

    @Override
    @Transactional
    public ChatResponseDTO.ChatDTO getChat(Long chatId, Integer pageSize, LocalDateTime cursorDateTime) {

        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        chatRepository.findByIdAndInactive(chatId, null)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        // 채팅에 참여 중인 회원만 채팅방 조회 가능
        if (!memberChatRepository.existsByMemberIdAndChatId(member.getId(), chatId)) {
            throw new ChatHandler(ErrorStatus.CHAT_PARTICIPANTS_ONLY_ALLOWED);
        }

        memberChatRepository.findAllByChatId(chatId).stream()
                .filter(memberChat -> !memberChat.getMember().equals(member))
                .flatMap(memberChat -> messageRepository.findAllByMemberChatId(memberChat.getId()).stream())
                .forEach(message -> {
                    message.setIsChecked(true);
                    messageRepository.save(message);
                });

        List<Message> messages = messageRepository.findMessagesByChatId(chatId, pageSize, cursorDateTime);

        return ChatResponseDTO.ChatDTO.toDTO(messages);
    }
}