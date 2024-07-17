package euclid.lyc_spring.apiPayload.exception.handler;

import euclid.lyc_spring.apiPayload.code.BaseErrorCode;
import euclid.lyc_spring.apiPayload.exception.GeneralException;

public class MemberHandler extends GeneralException {

    public MemberHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
