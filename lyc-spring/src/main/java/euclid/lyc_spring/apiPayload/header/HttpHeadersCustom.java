package euclid.lyc_spring.apiPayload.header;

import org.springframework.http.HttpHeaders;

public class HttpHeadersCustom extends HttpHeaders {

    public static final String ACCESSTOKEN = "access-token";

    public static final String REFRESHTOKEN = "refresh-token";

    public static final String TEMPTOKEN = "temp-token";
}
