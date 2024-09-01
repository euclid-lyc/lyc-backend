package euclid.lyc_spring.service.chat;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.ChatHandler;
import euclid.lyc_spring.apiPayload.exception.handler.MemberHandler;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.domain.Member;
import euclid.lyc_spring.domain.chat.Chat;
import euclid.lyc_spring.domain.chat.Schedule;
import euclid.lyc_spring.dto.request.ChatRequestDTO;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import euclid.lyc_spring.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatCommandServiceImpl implements ChatCommandService {

    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    private final CommissionRepository commissionRepository;
    private final ScheduleRepository scheduleRepository;

    private final MemberChatRepository memberChatRepository;

/*-------------------------------------------------- 채팅방 --------------------------------------------------*/

    @Override
    public ChatResponseDTO.ChatInactiveDTO terminateChat(Long chatId) {

        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Chat chat = chatRepository.findByIdAndInactive(chatId, null)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        // 채팅에 참여 중인 회원만 채팅방 종료 가능
        if (!memberChatRepository.existsByMemberIdAndChatId(member.getId(), chatId)) {
            throw new ChatHandler(ErrorStatus.CHAT_PARTICIPANTS_ONLY_ALLOWED);
        }

        chat.setInactive(LocalDateTime.now());
        return ChatResponseDTO.ChatInactiveDTO.builder().inactive(chat.getInactive()).build();
    }

/*-------------------------------------------------- 메시지 --------------------------------------------------*/
/*-------------------------------------------------- 일정 --------------------------------------------------*/

    @Override
    public ChatResponseDTO.ScheduleDTO createSchedule(Long chatId, ChatRequestDTO.ScheduleReqDTO scheduleReqDTO) {

        String loginId = SecurityUtils.getAuthorizedLoginId();
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new MemberHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Chat chat = chatRepository.findByIdAndInactive(chatId, null)
                .orElseThrow(() -> new ChatHandler(ErrorStatus.CHAT_NOT_FOUND));

        // 채팅에 참여 중인 회원만 일정 생성 가능
        if (!memberChatRepository.existsByMemberIdAndChatId(member.getId(), chatId)) {
            throw new ChatHandler(ErrorStatus.CHAT_PARTICIPANTS_ONLY_ALLOWED);
        }

        Schedule schedule = Schedule.builder()
                .date(scheduleReqDTO.getDate())
                .memo(scheduleReqDTO.getMemo())
                .chat(chat)
                .build();

        schedule = scheduleRepository.save(schedule);
        chat.addSchedule(schedule);
        return ChatResponseDTO.ScheduleDTO.toDTO(schedule);
    }

/*-------------------------------------------------- 사진 및 동영상 --------------------------------------------------*/

}
