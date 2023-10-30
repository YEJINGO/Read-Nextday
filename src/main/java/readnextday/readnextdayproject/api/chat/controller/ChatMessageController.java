package readnextday.readnextdayproject.api.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import readnextday.readnextdayproject.api.chat.dto.Message;
import readnextday.readnextdayproject.config.jwt.JwtUtils;
import readnextday.readnextdayproject.api.chat.dto.MessageType;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatMessageController {

    private final JwtUtils jwtUtils;
    private final RedisTemplate<String, Object> redisTemplate;
    private final ChannelTopic channelTopic;

    /**
     * websocket "/pub/api/chat/message/" 로 들어오는 메시징을 처리한다.
     * message.setSender(slackId) : 로그인 회원 정보로 대화명 설정
     * convertAndSend : Websocket 에 발행된 메시지를 redis 로 발행(publish)
     * @param message
     * @param token
     */
    @MessageMapping("/message")
    public void message(Message message, @Header("Authorization") String token) {
        String slackId = jwtUtils.getMember(token).getMember().getSlackId();
        message.setSender(slackId);

        if (MessageType.ENTER.equals(message.getType())) {
            message.setSender("[알림]");
            message.setMessage(slackId + "님이 입장하셨습니다.");
        }

        redisTemplate.convertAndSend(channelTopic.getTopic(), message);
    }
}
