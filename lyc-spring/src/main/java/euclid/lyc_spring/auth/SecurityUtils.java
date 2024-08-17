package euclid.lyc_spring.auth;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.GeneralException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Objects;

public class SecurityUtils {

    public static String getAuthorizedLoginId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new GeneralException(ErrorStatus.JWT_UNAUTHORIZED);
        }
        return authentication.getPrincipal().toString();
    }

}
