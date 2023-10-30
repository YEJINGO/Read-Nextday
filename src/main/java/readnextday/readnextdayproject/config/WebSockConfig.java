package readnextday.readnextdayproject.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

/**
 * 1. WebSocketMessageBrokerConfigurer 를 상속받아 STOMP로 메시지 처리 방법을 구성한다.
 * 2. configureMessageBroker 에서는 메시지를 중간에서 라우팅할 때 사용하는 메시지 브로커를 구성한다.
 * 3. enableSimpleBroker에서는 해당 주소를 구독하는 클라이언트에게 메시지를 보낸다.
    즉, 인자에는 구독 요청의 prefix를 넣고, 클라이언트에서 1번 채널을 구독하고자 할 때는 /sub/1 형식과 같은 규칙을 따라야 한다.
 * 4. setApplicationDestinationPrefixes에는 메시지 발행 요청의 prefix를 넣는다.
    즉, /pub로 시작하는 메시지만 해당 Broker에서 받아서 처리한다
 * 5. registerStompEndpoints : 클라이언트에서 WebSocket에 접속할 수 있는 endpoint를 지정한다.
 * 6. configureClientInboundChannel : 사용자가 웹 소켓 연결에 연결 될 때와 끊길 때 추가 기능(인증, 세션 관리 등)을 위해 인터셉터를 걸어주었다.
    인자에는 추가 기능을 구현한 StompHandler를 빈으로 등록하여 넣어주었다.
 */
@RequiredArgsConstructor
@Configuration
@EnableWebSocketMessageBroker
public class WebSockConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/sub");
        config.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws-stomp").setAllowedOrigins("*")
                .withSockJS(); // sock.js를 통하여 낮은 버전의 브라우저에서도 websocket이 동작할수 있게 합니다.
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}
