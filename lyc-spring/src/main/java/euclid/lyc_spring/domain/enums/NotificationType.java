package euclid.lyc_spring.domain.enums;

public enum NotificationType {
    REQUIRED, // 의뢰 요청
    APPROVED, // 의뢰 승인
    WAIT_FOR_TERMINATION, // 의뢰 종료 대기
    TERMINATED // 의뢰 종료
}
