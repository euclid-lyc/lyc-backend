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
