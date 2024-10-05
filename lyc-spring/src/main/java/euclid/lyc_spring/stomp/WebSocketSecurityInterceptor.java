package euclid.lyc_spring.stomp;

import euclid.lyc_spring.apiPayload.code.status.ErrorStatus;
import euclid.lyc_spring.apiPayload.exception.handler.JwtHandler;
import euclid.lyc_spring.auth.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.WebSocketHttpHeaders;

@Configuration
@RequiredArgsConstructor
public class WebSocketSecurityInterceptor implements ChannelInterceptor {

    private final JwtProvider jwtProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        if(StompCommand.CONNECT.equals(headerAccessor.getCommand())) {
            validateToken(headerAccessor);
        }
        return message;
    }

    private void validateToken(StompHeaderAccessor headerAccessor) {
        String accessToken = jwtProvider.resolveToken(headerAccessor.getFirstNativeHeader(WebSocketHttpHeaders.AUTHORIZATION));
        for (String header : headerAccessor.toMap().keySet()) {
            System.out.println("Header: " + header + " Value: " + headerAccessor.getFirstNativeHeader(header));
        }
        if (accessToken == null) {
            throw new JwtHandler(ErrorStatus.JWT_NULL_TOKEN);
        }
        jwtProvider.validateToken(accessToken);
    }
}
