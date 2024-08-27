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
    JWT_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "AA4008", "액세스 토큰이 만료되었습니다."),

    // member
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "MEMBER4001", "사용자가 존재하지 않습니다."),
    MEMBER_ALREADY_EXIST(HttpStatus.FORBIDDEN, "MEMBER4002", "이메일이 일치하는 사용자가 이미 존재합니다."),
    NOT_FOLLOWING(HttpStatus.NOT_FOUND, "MEMBER4003", "팔로우 관계가 존재하지 않습니다."),
    BLOCKING_MEMBER(HttpStatus.FORBIDDEN, "MEMBER4004", "차단된 유저입니다"),
    MEMBER_NOT_BLOCKING(HttpStatus.NOT_FOUND, "MEMBER4005", "차단 관계가 존재하지 않습니다."),
    MEMBER_DUPLICATED_LOGIN_ID(HttpStatus.BAD_REQUEST, "MEMBER4006", "로그인 아이디가 이미 존재합니다."),
    MEMBER_INVALID_LOGIN_PW(HttpStatus.BAD_REQUEST, "MEMBER4007", "패스워드와 패스워드 확인이 일치하지 않습니다."),
    MEMBER_ALREADY_FOLLOWING(HttpStatus.FORBIDDEN, "MEMBER4008", "이미 팔로우 중인 멤버입니다."),
    BLOCKED_MEMBER(HttpStatus.FORBIDDEN, "MEMBER4009", "차단 당한 멤버입니다."),

    // posting
    POSTING_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4041", "포스팅이 존재하지 않습니다."),
    SAVED_POSTING_NOT_FOUND(HttpStatus.NOT_FOUND, "POST4042", "포스팅이 저장되어 있지 않습니다."),
    POSTING_ALREADY_SAVED(HttpStatus.FORBIDDEN, "POST4044", "이미 저장한 게시물입니다."),
    MEMBER_NOT_LIKED_POSTING(HttpStatus.FORBIDDEN, "POST4043", "해당 유저는 이 게시물에 좋아요를 누르지 않았습니다."),
    POSTING_ALREADY_LIKED(HttpStatus.FORBIDDEN, "POST4045", "이미 좋아요를 눌렀습니다."),
    SAVED_POSTING_CANNOT_ACCESS(HttpStatus.UNAUTHORIZED, "POST4046", "저장된 게시글에 접근할 수 없습니다."),
    POSTING_NOT_LIKED(HttpStatus.FORBIDDEN, "POST4047", "좋아요 기록이 없습니다."),
    WRITER_ONLY_ALLOWED(HttpStatus.BAD_REQUEST, "POST4048", "게시글 작성자에게만 권한이 부여됩니다."),
    POSTING_CANNOT_SAVED_BY_WRITER(HttpStatus.BAD_REQUEST, "POST4049", "자신의 게시글을 저장할 수 업습니다."),

    // Commission
    COMMISSION_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMISSION4041", "의뢰를 찾을 수 없습니다."),
    COMMISSION_OTHER_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMISSION4042", "기타사항을 찾을 수 없습니다."),
    COMMISSION_STYLE_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMISSION4043", "원하는 스타일을 찾을 수 없습니다."),
    COMMISSION_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMISSION4044", "기본 정보를 찾을 수 없습니다."),

    // clothes
    CLOTHES_NOT_FOUND(HttpStatus.NOT_FOUND, "CLOTHES4041", "옷장 게시들이 존재하지 않습니다."),
    CLOTHES_IMAGE_NOT_FOUND(HttpStatus.NOT_FOUND, "CLOTHES4001", "옷장 이미지를 찾을 수 없습니다."),
    CLOTHES_TEXT_NOT_FOUND(HttpStatus.NOT_FOUND, "CLOTHES4002", "옷장 텍스트를 찾을 수 없습니다."),
    CLOTHES_BAD_QUERY(HttpStatus.BAD_REQUEST, "CLOTHES4003", "type 쿼리에는 image와 text만 입력할 수 있습니다."),

    // CHAT
    CHAT_NOT_FOUND(HttpStatus.NOT_FOUND, "CHAT4041", "채팅을 찾을 수 없습니다."),

    // WEATHER
    _WEATHER_NOT_FOUND(HttpStatus.NOT_FOUND, "WEATHER4001", "날씨 정보를 찾을 수 없습")

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
