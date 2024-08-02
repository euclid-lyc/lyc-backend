package euclid.lyc_spring.apiPayload.code.status;

import euclid.lyc_spring.apiPayload.code.BaseErrorCode;
import euclid.lyc_spring.apiPayload.code.ErrorReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorStatus implements BaseErrorCode {

    // common
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "COMMON500", "서버 에러, 관리자에게 문의 바랍니다."),
    BAD_REQUEST(HttpStatus.BAD_REQUEST,"COMMON400","잘못된 요청입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED,"COMMON401","인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "COMMON403", "금지된 요청입니다."),

    // member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4041", "사용자가 존재하지 않습니다."),
    MEMBER_ALREADY_EXIST(HttpStatus.FORBIDDEN, "MEMBER4031", "이메일이 일치하는 사용자가 이미 존재합니다."),
    NOT_FOLLOWING(HttpStatus.NOT_FOUND, "MEMBER4042", "팔로우 관계가 존재하지 않습니다."),
    MEMBER_ALREADY_BLOCKED(HttpStatus.FORBIDDEN, "MEMBER4043", "이미 차단된 유저입니다"),
    MEMBER_NOT_BLOCKED(HttpStatus.NOT_FOUND, "MEMBER4044", "차단 관계가 존재하지 않습니다."),

    // posting
    POSTING_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4041", "포스팅이 존재하지 않습니다."),
    SAVED_POSTING_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4042", "포스팅이 저장되어 있지 않습니다."),
    POSTING_ALREADY_SAVED(HttpStatus.FORBIDDEN, "POST4044", "이미 저장한 게시물입니다."),
    MEMBER_NOT_LIKED_POSTING(HttpStatus.FORBIDDEN, "POST4043", "해당 유저는 이 게시물에 좋아요를 누르지 않았습니다.")


    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ErrorReasonDTO getReason() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .build();
    }

    @Override
    public ErrorReasonDTO getReasonHttpStatus() {
        return ErrorReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(false)
                .httpStatus(httpStatus)
                .build();
    }
}
