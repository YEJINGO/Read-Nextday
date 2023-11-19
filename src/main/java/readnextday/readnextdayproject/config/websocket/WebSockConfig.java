package readnextday.readnextdayproject.config.websocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import readnextday.readnextdayproject.exception.chatException.StompExceptionHandler;

/**
 * 1. WebSocketMessageBrokerConfigurer 를 상속받아 STOMP로 메시지 처리 방법을 구성한다.
 * 2. configureMessageBroker 에서는 메시지를 중간에서 라우팅할 때 사용하는 메시지 브로커를 구성한다.
 * 3. enableSimpleBroker에서는 해당 주소를 구독하는 클라이언트에게 메시지를 보낸다.
 * 즉, 인자에는 구독 요청의 prefix를 넣고, 클라이언트에서 1번 채널을 구독하고자 할 때는 /sub/1 형식과 같은 규칙을 따라야 한다.
 * 4. setApplicationDestinationPrefixes에는 메시지 발행 요청의 prefix를 넣는다.
 * 즉, /pub로 시작하는 메시지만 해당 Broker에서 받아서 처리한다
 * 5. registerStompEndpoints : 클라이언트에서 WebSocket에 접속할 수 있는 endpoint를 지정한다.
 * 6. configureClientInboundChannel : 사용자가 웹 소켓 연결에 연결 될 때와 끊길 때 추가 기능(인증, 세션 관리 등)을 위해 인터셉터를 걸어주었다.
 * 인자에는 추가 기능을 구현한 StompHandler를 빈으로 등록하여 넣어주었다.
 */
@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;
    private final StompExceptionHandler stompExceptionHandler;


    /**
     * 서버와 처음 연결해주는 부분
     * Endpoint 는 클라이언트가 서버로 연결하는 특정 경로 또는 주소를 의미
     * WebSocket Open
     */
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .setErrorHandler(stompExceptionHandler)
                .addEndpoint("/ws")
                .setAllowedOriginPatterns("*");
//                .withSockJS(); // sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작할 수 있게 합니다.

    }

    /**
     * 메시지 송수신을 처리하는 부분
     * 메시지 브로커는 서버와 클라이언트 간의 메시지 교환을 관리하고 중계하는 시스템
     * 클라이언트와 서버 간의 메시지 교환을 관리하고 구성할 수 있으며, 클라이언트는 "/sub" 주제를 구독하여 서버로부터 메시지를 수신하고, "/pub" 주제를 사용하여 서버로 메시지를 보낼 수 있게 된다.
     * 이것은 STOMP 프로토콜을 통해 구현된 실시간 메시징 시스템을 구성하는데 사용된다.
     * enableSimpleBroker : sub로 보내면 이곳을 한번 거쳐서 프론트에 데이터전달, sub로 보내면 이곳을 한번 거쳐서 프론트에 데이터전달,클라이언트는 이 주제를 구독하여 서버에서 전송되는 메시지를 수신할 수 있음
     * setApplicationDestinationPrefixes : pub 로 데이터를 받으면 이곳을 한번 거쳐서 UIR 만 MessageMapping 에 매핑된다. ex) pub/chat/message 라면 pub 을 제외하고 /chat/message 를 @MessageMaping 에 매핑한다.
     */
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    /**
     * WebSocket 연결의 클라이언트 인바운드 채널을 구성하는 메서드를 나타낸다.
     */

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
