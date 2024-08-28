package euclid.lyc_spring.apiPayload.exception.handler;

import euclid.lyc_spring.apiPayload.code.BaseErrorCode;
import euclid.lyc_spring.apiPayload.exception.GeneralException;

public class S3Handler extends GeneralException {
    public S3Handler(BaseErrorCode errorCode) {
        super(errorCode);
    }
}
