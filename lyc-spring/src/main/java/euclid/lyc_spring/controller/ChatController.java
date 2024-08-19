package euclid.lyc_spring.controller;

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
            """)
    @GetMapping("/chats")
    public void getAllChats() {}

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
    public void getChatMembers(@PathVariable Long chatId) {}

    @Tag(name = "Chat - General", description = "채팅방 관련 API")
    @Operation(summary = "채팅방 나가기", description = """
            """)
    @PatchMapping("/chats/{chatId}")
    public void terminateChat() {}

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
            """)
    @PostMapping("/chats/{chatId}/schedules")
    public void createSchedule(@PathVariable Long chatId) {}

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
