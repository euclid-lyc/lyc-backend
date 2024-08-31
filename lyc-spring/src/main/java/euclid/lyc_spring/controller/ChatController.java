package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.dto.request.ChatRequestDTO;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import euclid.lyc_spring.service.chat.ChatCommandService;
import euclid.lyc_spring.service.chat.ChatQueryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc")
public class ChatController {

    private final ChatQueryService chatQueryService;
    private final ChatCommandService chatCommandService;

/*-------------------------------------------------- 채팅방 --------------------------------------------------*/

    @Tag(name = "Chat - General", description = "채팅방 관련 API")
    @Operation(summary = "채팅방 목록 불러오기", description = """
            로그인한 회원과 채팅을 주고받은 회원 목록을 최근 메시지를 주고받은 순으로 불러옵니다.
            
            오프셋 기반 페이징이 적용됩니다.
            """)
    @GetMapping("/chats")
    public ApiResponse<ChatResponseDTO.ChatPreviewListDTO> getAllChats() {
        ChatResponseDTO.ChatPreviewListDTO chatMemberPreviewDTO = chatQueryService.getAllChats();
        return ApiResponse.onSuccess(SuccessStatus._CHAT_LIST_FOUND, chatMemberPreviewDTO);
    }

    @Tag(name = "Chat - General", description = "채팅방 관련 API")
    @Operation(summary = "채팅방 불러오기", description = """
            로그인한 회원과 특정 회원의 채팅방 정보 및 메시지 목록을 반환합니다.
            """)
    @GetMapping("/chats/{chatId}")
    public void getChat(@PathVariable Long chatId) {}

    @Tag(name = "Chat - General", description = "채팅방 관련 API")
    @Operation(summary = "대화상대 목록 불러오기", description = """
            채팅에 참여하는 회원의 목록을 반환합니다.
            """)
    @GetMapping("/chats/{chatId}/members")
    public ApiResponse<ChatResponseDTO.ChatMemberListDTO> getChatMembers(@PathVariable Long chatId) {
        ChatResponseDTO.ChatMemberListDTO chatMemberListDTO = chatQueryService.getChatMembers(chatId);
        return ApiResponse.onSuccess(SuccessStatus._CHAT_MEMBERS_FOUND, chatMemberListDTO);
    }

    @Tag(name = "Chat - General", description = "채팅방 관련 API")
    @Operation(summary = "채팅방 나가기", description = """
            현재 진행중인 채팅을 종료합니다.
            
            의뢰가 종료되지 않은 경우 의뢰를 먼저 종료한 후 채팅을 종료할 수 있습니다.
            
            채팅 종료 시 Chat이 비활성화 되며 30일 이후 DB에서 자동으로 삭제됩니다.
            """)
    @PatchMapping("/chats/{chatId}")
    public ApiResponse<ChatResponseDTO.ChatInactiveDTO> terminateChat(@PathVariable Long chatId) {
        ChatResponseDTO.ChatInactiveDTO chatInactiveDTO = chatCommandService.terminateChat(chatId);
        return ApiResponse.onSuccess(SuccessStatus._CHAT_DISABLED, chatInactiveDTO);
    }

/*-------------------------------------------------- 메시지 --------------------------------------------------*/

    @Tag(name = "Chat - Message", description = "채팅방 메시지 관련 API")
    @Operation(summary = "메시지 전송하기 (텍스트)", description = """
            """)
    @PostMapping("/chats/{chatId}/messages/texts")
    public void sendMessageByText(@PathVariable Long chatId) {}

    @Tag(name = "Chat - Message", description = "채팅방 메시지 관련 API")
    @Operation(summary = "메시지 전송하기 (이미지)", description = """
            """)
    @PostMapping("/chats/{chatId}/messages/images")
    public void sendMessageByImage(@PathVariable Long chatId) {}

/*-------------------------------------------------- 일정 --------------------------------------------------*/

    @Tag(name = "Chat - Schedule", description = "채팅방 일정 관련 API")
    @Operation(summary = "월별 일정 불러오기", description = """
            """)
    @GetMapping("/chats/{chatId}/schedules")
    public void getMonthlySchedule(@PathVariable Long chatId) {}

    @Tag(name = "Chat - Schedule", description = "채팅방 일정 관련 API")
    @Operation(summary = "일정 생성하기", description = """
            채팅방에 의뢰와 관련된 일정을 추가합니다.
            """)
    @PostMapping("/chats/{chatId}/schedules")
    public ApiResponse<ChatResponseDTO.ScheduleDTO> createSchedule(
            @PathVariable Long chatId,
            @RequestBody ChatRequestDTO.ScheduleReqDTO scheduleReqDTO) {
        ChatResponseDTO.ScheduleDTO scheduleDTO = chatCommandService.createSchedule(chatId, scheduleReqDTO);
        return ApiResponse.onSuccess(SuccessStatus._CHAT_COMMISSION_SCHEDUlE_CREATED, scheduleDTO);
    }

/*-------------------------------------------------- 사진 및 동영상 --------------------------------------------------*/

    @Tag(name = "Chat - Image", description = "채팅방 사진 관련 API")
    @Operation(summary = "사진 및 동영상 목록 불러오기", description = """
            """)
    @GetMapping("/chats/{chatId}/images")
    public void getAllChatImages(@PathVariable Long chatId) {}

    @Tag(name = "Chat - Image", description = "채팅방 사진 관련 API")
    @Operation(summary = "사진 및 동영상 불러오기", description = """
            """)
    @GetMapping("/chats/{chatId}/images/{imageId}")
    public void getChatImage(
            @PathVariable Long chatId, @PathVariable Long imageId) {}

    @Tag(name = "Chat - Image", description = "채팅방 사진 관련 API")
    @Operation(summary = "사진 및 동영상 미리보기 불러오기", description = """
            """)
    @GetMapping("/chats/{chatId}/images/{imageId}/preview")
    public void getChatImagePreview(
            @PathVariable Long chatId, @PathVariable Long imageId) {}
}
