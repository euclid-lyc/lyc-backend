package euclid.lyc_spring.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, unique = true)
    private Long id;

    @Column(nullable = false)
    private String refreshToken;

    @Column(nullable = false)
    private String loginId;

    @Builder
    public RefreshToken(String refreshToken, String loginId) {
        this.refreshToken = refreshToken;
        this.loginId = loginId;
    }

    public RefreshToken updateToken(String token) {
        this.refreshToken = token;
        return this;
    }


}
