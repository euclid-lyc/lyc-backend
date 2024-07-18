package euclid.lyc_spring.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Feed", description = "피드 기능 관련 API")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class FeedController {


}
