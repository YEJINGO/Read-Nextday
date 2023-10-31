package readnextday.readnextdayproject.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageDeliveryException;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;
import readnextday.readnextdayproject.config.jwt.JwtUtils;

import java.io.IOException;

import static readnextday.readnextdayproject.config.jwt.JwtProperties.HEADER_STRING;
import static readnextday.readnextdayproject.config.jwt.JwtProperties.TOKEN_PREFIX;

@Slf4j
@Component
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtUtils jwtUtils;


    /**
     * Websocket 연결 시 요청 header 의 jwt token 유효성을 검증하는 코드를 추가한다. 유효하지 않은 JWT 토큰일 경우, websocket을 연결하지 않고 예외 처리 한다.
     * headerAccessor : Websocket 프로토콜에서 사용되는 헤더 정보를 추출하기 위해 stompHeaderAccessor 를 사용하여 메시지를 매핑한다.
     * @param message
     * @param channel
     * @return
     */
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(message);
        // 헤더 토큰 얻기
        String authorizationHeader = String.valueOf(headerAccessor.getNativeHeader(HEADER_STRING));
        if (authorizationHeader == null || authorizationHeader.equals("null")) {
            throw new MessageDeliveryException("메시지 예외 - 유효하지 않은 토큰입니다.");
        }
        String token = authorizationHeader.replace(TOKEN_PREFIX,"");

        try {
            jwtUtils.stompValidationJwt(token);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return message;
    }
}
