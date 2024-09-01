package euclid.lyc_spring.apiPayload.exception.handler;

import euclid.lyc_spring.apiPayload.code.BaseErrorCode;
import euclid.lyc_spring.apiPayload.exception.GeneralException;

public class CommissionHandler extends GeneralException {

    public CommissionHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
