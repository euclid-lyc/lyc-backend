package euclid.lyc_spring.repository.token;

import euclid.lyc_spring.auth.JwtProvider;
import io.jsonwebtoken.Claims;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.*;

@Getter
@Repository
public class TokenBlackListRepositoryImpl implements TokenBlackListRepository {

    private final JwtProvider jwtProvider;
    private final Map<String, Date> tokenBlackList;

    public TokenBlackListRepositoryImpl(JwtProvider jwtProvider) {
        this.jwtProvider = jwtProvider;
        this.tokenBlackList = new HashMap<>();
    }

    @Override
    public void addTokenToBlackList(String accessToken) {
        Claims claims = jwtProvider.getClaims(accessToken);
        Date expiration = claims.getExpiration();
        tokenBlackList.put(accessToken, expiration);
    }

    @Override
    public boolean isBlackListed(String accessToken) {
        removeExpiredTokens();
        return tokenBlackList.containsKey(accessToken);
    }

    private void removeExpiredTokens() {
        Date now = new Date();
        for (Map.Entry<String, Date> entry : tokenBlackList.entrySet()) {
            // 토큰이 이미 만료되었다면 삭제
            if (entry.getValue().before(now)) {
                tokenBlackList.remove(entry.getKey());
            }
        }
    }

}
