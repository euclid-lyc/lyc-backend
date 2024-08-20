package euclid.lyc_spring.apiPayload.exception.handler;

import euclid.lyc_spring.apiPayload.code.BaseErrorCode;
import euclid.lyc_spring.apiPayload.exception.GeneralException;

public class ClothesHandler extends GeneralException {

    public ClothesHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
