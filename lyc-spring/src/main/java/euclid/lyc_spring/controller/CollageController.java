package euclid.lyc_spring.controller;

import euclid.lyc_spring.service.collage.CollageCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Collage", description = "콜라주 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/lyc")
public class CollageController {

    private final CollageCommandService collageCommandService;

    @Operation(summary = "[구현중]콜라주 생성하기", description = """
           """)
    @PostMapping("/collages")
    public void createCollage() {}

    @Operation(summary = "[구현중] 콜라주 공유하기", description = """
            """)
    @PostMapping("/chats/{chatId}/messages/collages")
    public void sendMessageByImage() {}

}
