package euclid.lyc_spring.apiPayload.code.status;

import euclid.lyc_spring.apiPayload.code.BaseCode;
import euclid.lyc_spring.apiPayload.code.ReasonDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum SuccessStatus implements BaseCode {

    // common
    _OK(HttpStatus.OK, "COMMON200", "성공"),

    // clothes
    _CLOTHES_BY_IMAGE_CREATED(HttpStatus.CREATED, "CLOTHES2001", "옷장 게시글(이미지) 작성 완료"),
    _CLOTHES_BY_TEXT_CREATED(HttpStatus.CREATED, "CLOTHES2002", "옷장 게시글(텍스트) 작성 완료"),
    _CLOTHES_LIST_FETCHED(HttpStatus.OK, "CLOTHES2003", "옷장 게시글 목록 불러오기 완료"),
    _CLOTHES_FETCHED(HttpStatus.OK, "CLOTHES2003", "옷장 게시글 불러오기 완료"),

    // authentication & authorization
    _VERIFICATION_CODE_SENT(HttpStatus.CREATED, "AA2001", "인증 코드 전송 완료"),
    _VERIFICATION_CODE_CHECKED(HttpStatus.OK, "AA2002", "인증 코드 검증 완료"),

    // member
    _MEMBER_CREATED(HttpStatus.CREATED, "MEMBER2001", "회원가입 완료"),
    _MEMBER_FOUND(HttpStatus.OK, "MEMBER2002", "회원 조회 완료"),
    _TODAY_DIRECTOR_FETCHED(HttpStatus.OK, "MEMBER2003", "오늘의 디렉터 불러오기 완료"),
    _MEMBER_SIGNED_IN(HttpStatus.CREATED, "MEMBER2004", "로그인 완료"),
    _MEMBER_FOLLOWER_FOUND(HttpStatus.OK, "MEMBER2005", "팔로워 조회 완료"),
    _MEMBER_FOLLOWING_FOUND(HttpStatus.OK, "MEMBER2006", "팔로잉 조회 완료"),
    _MEMBER_FOLLOWED(HttpStatus.CREATED, "MEMBER2007", "회원 팔로우 완료"),
    _MEMBER_FOLLOWING_DELETED(HttpStatus.OK, "MEMBER2008", "회원 팔로잉 삭제 완료"),
    _MEMBER_BLOCKED(HttpStatus.CREATED, "MEMBER2009", "회원 차단 완료"),
    _MEMBER_BLOCK_CANCELED(HttpStatus.OK, "MEMBER2010", "회원 차단 해제 완료"),
    _MEMBER_SIGNED_OUT(HttpStatus.CREATED, "MEMBER2011", "로그아웃 완료"),
    _MEMBER_WITHDRAWN(HttpStatus.OK, "MEMBER2012", "회원 탈퇴 완료"),
    _MEMBER_LOGIN_ID_FOUND(HttpStatus.OK, "MEMBER2013", "회원 아이디 조회 완료"),
    _MEMBER_PW_CHANGED(HttpStatus.OK, "MEMBER2014", "회원 패스워드 변경 완료"),

    // coordie
    _MEMBER_COORDIES_FETCHED(HttpStatus.OK, "COORDIE2001", "유저의 코디 목록 불러오기 완료"),
    _SAVED_COORDIES_FETCHED(HttpStatus.OK, "COORDIE2002", "저장한 코디 목록 불러오기 완료"),

    // REVIEW
    _MEMBER_REVIEWS_FETCHED(HttpStatus.OK, "REVIEW2001", "유저의 리뷰 목록 불러오기 완료"),

    // POSTING(GENERAL)
    _SAVED_POSTING_FETCHED(HttpStatus.OK, "POSTING2001", "저장한 게시글 불러오기 완료"),
    _SAVED_POSTING_CREATED(HttpStatus.CREATED, "POSTING2002", "게시글 저장 완료"),
    _IS_CLICKED_LIKE_FETCHED(HttpStatus.OK, "POSTING2003", "게시글 좋아요 클릭 여부 불러오기 완료"),
    _IS_SAVED_POSTING_FETCHED(HttpStatus.OK, "POSTING2004", "게시글 저장 여부 불러오기 완료"),
    _POSTING_CREATED(HttpStatus.CREATED, "POSTING2005", "게시글 작성 완료"),
    _POSTING_DELETED(HttpStatus.OK, "POSTING2006", "게시글 삭제 완료"),
    _SAVED_POSTING_DELETED(HttpStatus.OK, "POSTING2007", "저장한 게시글 삭제 완료"),
    _POSTING_LIKED(HttpStatus.CREATED, "POSTING2008", "게시글 좋아요 완료"),
    _POSTING_LIKE_CANCELED(HttpStatus.OK, "POSTING2009", "게시글 좋아요 취소 완료"),

    // FEED
    _RECENT_TEN_FEEDS_FETCHED(HttpStatus.OK, "FEED2001", "최신 피드 10개 불러오기 완료"),

    // COMMISSION
    _COMMISSION_CREATED(HttpStatus.CREATED, "COMMISSION2001", "의뢰서 작성 완료"),
    _COMMISSION_LIST_FETCHED(HttpStatus.OK, "COMMISSION2002", "의뢰함 불러오기 완료"),
    _COMMISSION_ACCEPTED(HttpStatus.ACCEPTED, "COMMISSION2003", "의뢰 승낙 완료"),
    _COMMISSION_DECLINED(HttpStatus.OK, "COMMISSION2004", "의뢰 거절 완료"),
    _COMMISSION_FETCHED(HttpStatus.OK, "COMMISSION2005", "의뢰서 불러오기 완료"),
    _COMMISSION_UPDATED(HttpStatus.OK, "COMMISSION2006", "의뢰서 수정 완료"),
    _COMMISSION_REQUEST_TERMINATION(HttpStatus.OK, "COMMISSION2007", "의뢰 종료 요청 완료"),
    _COMMISSION_TERMINATION(HttpStatus.OK, "COMMISSION2008", "의뢰 종료 승낙 완료"),
    _COMMISSION_TERMINATION_DECLINED(HttpStatus.OK, "COMMISSION2009", "의뢰 종료 거절 완료"),

    // WEATHER
    _WEATHER_FETCHED(HttpStatus.OK, "WEATHER2001", "오늘 날씨 불러오기 완료"),

    // CHAT
    _CHAT_LIST_FOUND(HttpStatus.OK, "CHAT2001", "채팅방 목록 조회 완료"),
    _CHAT_MEMBERS_FOUND(HttpStatus.OK, "CHAT2002", "채팅방 대화 상대 목록 조회 완료"),
    _CHAT_DISABLED(HttpStatus.OK, "CHAT2003", "채팅방 종료 완료"),
    _CHAT_COMMISSION_SCHEDULE_CREATED(HttpStatus.CREATED, "CHAT2004", "채팅방 의뢰 일정 추가 완료"),
    _CHAT_COMMISSION_SCHEDULE_FOUND(HttpStatus.OK, "CHAT2005", "채팅방 의뢰 일정 조회 완료"),
    _CHAT_IMAGE_LIST_FOUND(HttpStatus.OK, "CHAT2006", "채팅방 이미지 목록 조회 완료"),

    ;

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    @Override
    public ReasonDTO getReason() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .build();
    }

    @Override
    public ReasonDTO getReasonHttpStatus() {
        return ReasonDTO.builder()
                .message(message)
                .code(code)
                .isSuccess(true)
                .httpStatus(httpStatus)
                .build();
    }
}
