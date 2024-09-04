package euclid.lyc_spring.controller;

import euclid.lyc_spring.apiPayload.ApiResponse;
import euclid.lyc_spring.apiPayload.code.status.SuccessStatus;
import euclid.lyc_spring.auth.SecurityUtils;
import euclid.lyc_spring.dto.request.ChatRequestDTO;
import euclid.lyc_spring.dto.response.ChatResponseDTO;
import euclid.lyc_spring.service.chat.ChatCommandService;
import euclid.lyc_spring.service.chat.ChatQueryService;
import euclid.lyc_spring.service.s3.S3ImageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.*;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc")
public class ChatController {

    private final ChatQueryService chatQueryService;
    private final ChatCommandService chatCommandService;
    private final S3ImageService s3ImageService;

    private final SimpMessagingTemplate simpMessagingTemplate;

/*-------------------------------------------------- 채팅방 --------------------------------------------------*/

    @Tag(name = "Chat - General", description = "채팅방 관련 API")
    @Operation(summary = "[구현완료] 채팅방 목록 불러오기", description = """
            로그인한 회원과 채팅을 주고받은 회원 목록을 최근 메시지를 주고받은 순으로 불러옵니다.
            
            오프셋 기반 페이징이 적용됩니다. (이거는 커서 기반 페이징 하다가 때려치움;;)
            """)
    @GetMapping("/chats")
    public ApiResponse<ChatResponseDTO.ChatPreviewListDTO> getAllChats(
            @RequestParam @Min(0) Integer pageNum,
            @RequestParam @Min(1) Integer pageSize
            ) {
        ChatResponseDTO.ChatPreviewListDTO chatMemberPreviewDTO = chatQueryService.getAllChats(PageRequest.of(pageNum, pageSize));
        return ApiResponse.onSuccess(SuccessStatus._CHAT_LIST_FOUND, chatMemberPreviewDTO);
    }

    @Tag(name = "Chat - General", description = "채팅방 관련 API")
    @Operation(summary = "[구현중] 채팅방 불러오기", description = """
            """)
    @PostMapping("/chats/{chatId}")
    public void getChat(@PathVariable Long chatId) {}

    @Tag(name = "Chat - General", description = "채팅방 관련 API")
    @Operation(summary = "[구현완료] 대화상대 목록 불러오기", description = """
            채팅에 참여하는 회원의 목록을 반환합니다.
            """)
    @GetMapping("/chats/{chatId}/members")
    public ApiResponse<ChatResponseDTO.ChatMemberListDTO> getChatMembers(@PathVariable Long chatId) {
        ChatResponseDTO.ChatMemberListDTO chatMemberListDTO = chatQueryService.getChatMembers(chatId);
        return ApiResponse.onSuccess(SuccessStatus._CHAT_MEMBERS_FOUND, chatMemberListDTO);
    }

    @Tag(name = "Chat - General", description = "채팅방 관련 API")
    @Operation(summary = "[구현완료] 채팅방 나가기", description = """
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

    @Tag(name = "Chat - General", description = "채팅방 관련 API")
    @Operation(summary = "[구현중] 메시지 전송하기", description = """
            HTTP 요청이 아니라 스웨거 화면에 나오진 않을 거지만... 로그인한 회원과 특정 회원의 채팅방 정보 및 메시지 목록을 반환합니다.
            
            텍스트 : { "content" : "메시지 내용", "isText" : true, "accessToken" : "Bearer {token}" }
            
            이미지 : { "content" : "이미지 Url", "isText" : false, "accessToken" : "Bearer {token}" }
            
            이미지 전송의 경우 이미지 업로드를 먼저 수행한 후 전송 바랍니다.
            """)
    @MessageMapping("/chats/{chatId}")
    @SendTo("/sub/{chatId}")
    public ApiResponse<ChatResponseDTO.MessageInfoDTO> sendMessage(
            @DestinationVariable("chatId") Long chatId,
            ChatRequestDTO.MessageDTO messageDTO) {
        ChatResponseDTO.MessageInfoDTO messageInfoDTO = chatCommandService.saveMessage(chatId, messageDTO);
        return ApiResponse.onSuccess(SuccessStatus._CHAT_MESSAGE_SENT, messageInfoDTO);
    }

    @Tag(name = "Chat - Message", description = "채팅방 메시지 관련 API")
    @Operation(summary = "[구현중] 메시지 전송하기 (이미지) - 이미지 업로드", description = """
            """)
    @PostMapping(value = "/chats/{chatId}/messages/images", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<String> uploadImageMessage(
            @PathVariable Long chatId,
            @RequestPart(required = false) MultipartFile image) {
        String imageUrl = s3ImageService.upload(image);
        return ApiResponse.onSuccess(SuccessStatus._CHAT_IMAGE_UPLOADED, imageUrl);
    }

/*-------------------------------------------------- 일정 --------------------------------------------------*/

    @Tag(name = "Chat - Schedule", description = "채팅방 일정 관련 API")
    @Operation(summary = "[구현완료] 일정 불러오기", description = """
            채팅방에 추가한 일정 목록을 불러옵니다.
            
            쿼리 값에 따라 날짜별로 일정을 불러오거나 월별로 일정을 불러올 수 있습니다.
            """)
    @GetMapping("/chats/{chatId}/schedules")
    public ApiResponse<ChatResponseDTO.ScheduleListDTO> getSchedules(
            @PathVariable Long chatId,
            @RequestParam @Range(min = -999999999, max = 999999999) Integer year,
            @RequestParam @Range(min = 1, max = 12)Integer month,
            @RequestParam(required = false) @Range(min = 1, max = 31) Integer day) {
        ChatResponseDTO.ScheduleListDTO scheduleListDTO;
        if (day == null) {
            scheduleListDTO = chatQueryService.getSchedules(chatId, year, month);
        } else {
            scheduleListDTO = chatQueryService.getSchedules(chatId, year, month, day);
        }
        return ApiResponse.onSuccess(SuccessStatus._CHAT_COMMISSION_SCHEDULE_FOUND, scheduleListDTO);
    }

    @Tag(name = "Chat - Schedule", description = "채팅방 일정 관련 API")
    @Operation(summary = "[구현완료] 일정 생성하기", description = """
            채팅방에 의뢰와 관련된 일정을 추가합니다.
            """)
    @PostMapping("/chats/{chatId}/schedules")
    public ApiResponse<ChatResponseDTO.ScheduleDTO> createSchedule(
            @PathVariable Long chatId,
            @RequestBody ChatRequestDTO.ScheduleReqDTO scheduleReqDTO) {
        ChatResponseDTO.ScheduleDTO scheduleDTO = chatCommandService.createSchedule(chatId, scheduleReqDTO);
        return ApiResponse.onSuccess(SuccessStatus._CHAT_COMMISSION_SCHEDULE_CREATED, scheduleDTO);
    }

/*-------------------------------------------------- 사진 및 동영상 --------------------------------------------------*/

    @Tag(name = "Chat - Image", description = "채팅방 사진 관련 API")
    @Operation(summary = "[구현완료] 사진 및 동영상 목록 불러오기", description = """
            채팅방에 전송한 사진 및 동영상의 목록을 조회합니다. (근데 우리 사진만 보내게 하면 안 될까...)
            
            커서 기반 페이징이 적용됩니다.
            """)
    @GetMapping("/chats/{chatId}/images")
    public ApiResponse<ChatResponseDTO.ImageListDTO> getAllChatImages(
            @PathVariable Long chatId,
            @RequestParam @Min(1) Integer pageSize,
            @RequestParam LocalDateTime cursorDateTime
            ) {
        ChatResponseDTO.ImageListDTO imageListDTO = chatQueryService.getAllChatImages(chatId, pageSize, cursorDateTime);
        return ApiResponse.onSuccess(SuccessStatus._CHAT_IMAGE_LIST_FOUND, imageListDTO);
    }

    @Tag(name = "Chat - Image", description = "채팅방 사진 관련 API")
    @Operation(summary = "[구현완료] 사진 및 동영상 불러오기", description = """
            채팅방에 전송한 특정 사진을 조회합니다.
            """)
    @GetMapping("/chats/{chatId}/images/{imageId}")
    public ApiResponse<ChatResponseDTO.ChatImageDTO> getChatImage(
            @PathVariable Long chatId, @PathVariable Long imageId) {
        ChatResponseDTO.ChatImageDTO chatImageDTO = chatQueryService.getChatImage(chatId, imageId);
        return ApiResponse.onSuccess(SuccessStatus._CHAT_IMAGE_FOUND, chatImageDTO);
    }

    //@Tag(name = "Chat - Image", description = "채팅방 사진 관련 API")
    //@Operation(summary = "사진 및 동영상 미리보기 불러오기", description = """
    //        """)
    //@GetMapping("/chats/{chatId}/images/{imageId}/preview")
    //public void getChatImagePreview(
    //        @PathVariable Long chatId, @PathVariable Long imageId) {}
}
