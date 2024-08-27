package euclid.lyc_spring.auth;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtConstant {

    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60 * 60; // 1시간
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 60 * 24 * 30; // 1개월
    public static final long TEMP_TOKEN_EXPIRE_TIME = 1000 * 60 * 3; // 3분

}
