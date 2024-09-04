package euclid.lyc_spring.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;

@Getter
public class ChatRequestDTO {

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class ScheduleReqDTO {
        private final LocalDate date;
        private final String memo;
    }

    @Getter
    @RequiredArgsConstructor
    @Builder
    public static class MessageDTO {
        private final String accessToken;
        private final String content;
        private final Boolean isText;
    }

}
