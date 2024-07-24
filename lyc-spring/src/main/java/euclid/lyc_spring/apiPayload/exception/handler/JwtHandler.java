package euclid.lyc_spring.apiPayload.exception.handler;

import euclid.lyc_spring.apiPayload.code.BaseErrorCode;
import euclid.lyc_spring.apiPayload.exception.GeneralException;

public class JwtHandler extends GeneralException {

    public JwtHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}