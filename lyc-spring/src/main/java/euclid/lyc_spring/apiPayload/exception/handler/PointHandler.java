package euclid.lyc_spring.apiPayload.exception.handler;

import euclid.lyc_spring.apiPayload.code.BaseErrorCode;
import euclid.lyc_spring.apiPayload.exception.GeneralException;

public class PointHandler extends GeneralException {

    public PointHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
