package euclid.lyc_spring.repository.mail;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.JwtHandler;
import lombok.Getter;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Getter
@Repository
public class VerificationCodeRepositoryImpl implements VerificationCodeRepository {

    private final Map<String, String> verificationCodes = new HashMap<>();

    @Override
    public void addVerificationCode(String tempToken, String code) {
        if (tempToken != null && code != null) {
            this.verificationCodes.put(tempToken, code);
        }
    }

    @Override
    public void removeVerificationCode(String tempToken) {
        if (tempToken != null) {
            this.verificationCodes.remove(tempToken);
        }

    }

    @Override
    public String getVerificationCode(String tempToken) {
        String code = verificationCodes.get(tempToken);
        if (code == null) {
            throw new JwtHandler(ErrorStatus.BAD_REQUEST);
        }
        return code;
    }

}
