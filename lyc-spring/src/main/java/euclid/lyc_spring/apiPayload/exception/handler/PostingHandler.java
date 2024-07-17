package euclid.lyc_spring.apiPayload.exception.handler;

import euclid.lyc_spring.apiPayload.code.BaseErrorCode;
import euclid.lyc_spring.apiPayload.exception.GeneralException;

public class PostingHandler extends GeneralException {

    public PostingHandler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
