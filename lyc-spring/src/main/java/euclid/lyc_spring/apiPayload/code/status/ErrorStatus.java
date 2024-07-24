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

    // Authentication & Authorization
    JWT_EXPIRED(HttpStatus.UNAUTHORIZED, "AA4001", "토큰이 만료되었습니다."),
    JWT_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AA4002", "인증에 실패하였습니다."),
    JWT_BEARER_NOT_VALIDATED(HttpStatus.UNAUTHORIZED, "AA4003", "Bearer 검증에 실패했습니다."),
    JWT_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "AA4004", "토큰이 유효하지 않습니다."),
    JWT_NULL_TOKEN(HttpStatus.UNAUTHORIZED, "AA4005", "토큰이 존재하지 않습니다."),
    LOGIN_ID_NOT_MATCHED(HttpStatus.NOT_FOUND, "AA4006", "로그인 아이디가 일치하지 않습니다."),
    LOGIN_PW_NOT_MATCHED(HttpStatus.NOT_FOUND, "AA4007", "로그인 패스워드가 일치하지 않습니다."),

    // member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4041", "사용자가 존재하지 않습니다."),
    MEMBER_ALREADY_EXIST(HttpStatus.BAD_REQUEST, "MEMBER4042", "이메일이 일치하는 사용자가 이미 존재합니다."),
    MEMBER_DUPLICATED_LOGIN_ID(HttpStatus.BAD_REQUEST, "MEMBER4043", "로그인 아이디가 이미 존재합니다."),
    MEMBER_INVALID_LOGIN_PW(HttpStatus.BAD_REQUEST, "MEMBER4044", "패스워드와 패스워드 확인이 일치하지 않습니다."),

    // posting
    POSTING_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4041", "포스팅이 존재하지 않습니다."),
    SAVED_POSTING_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4042", "포스팅이 저장되어 있지 않습니다."),

    // clothes
    CLOTHES_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CLOTHES4041", "옷장 이미지를 찾을 수 없습니다."),
    CLOTHES_TEXT_NOT_FOUND(HttpStatus.NOT_FOUND, "CLOTHES4042", "옷장 텍스트를 찾을 수 없습니다.")
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
