package euclid.lyc_spring.auth;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class JwtConstant {

    public static final long ACCESS_TOKEN_EXPIRE_TIME = 1000 * 60; // 1분
    public static final long REFRESH_TOKEN_EXPIRE_TIME = 1000L * 60 * 2; // * 60 * 24 * 30; // 1개월

}
