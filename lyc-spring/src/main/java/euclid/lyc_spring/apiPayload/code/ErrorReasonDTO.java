package euclid.lyc_spring.apiPayload.code;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class ErrorReasonDTO {

    private HttpStatus httpStatus;

    @Getter
    private final Boolean isSuccess;
    private final String code;
    private final String message;

}
