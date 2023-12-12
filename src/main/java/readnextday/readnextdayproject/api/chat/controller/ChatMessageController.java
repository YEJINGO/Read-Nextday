package readnextday.readnextdayproject.api.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.web.bind.annotation.RestController;
import readnextday.readnextdayproject.api.chat.dto.ChatMessageDto;
import readnextday.readnextdayproject.api.chat.pubsub.RedisPublisher;
import readnextday.readnextdayproject.api.chat.service.ChatMessageService;
import readnextday.readnextdayproject.api.chat.service.ChatRoomService;

import java.time.LocalDateTime;
import java.util.Objects;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatMessageController {

    private final RedisPublisher redisPublisher;
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;


    /**
     * websocket "/pub/message/" 로 들어오는 메시징을 처리한다.
     * message.setSender(slackId) : 로그인 회원 정보로 대화명 설정
     * convertAndSend : Websocket 에 발행된 메시지를 redis 로 발행(publish)
     * <p>
     * 순서
     */
    @MessageMapping("/message")
    public void message(ChatMessageDto chatMessageDto, @Header("Authentication") String token)  {

        chatRoomService.enterChatRoom(chatMessageDto.getRedisRoomId());
        chatMessageDto.setMessageCreatedAt(LocalDateTime.now());
        ChatMessageDto cmd = chatMessageDto;

        ChannelTopic topic = chatRoomService.getTopic(chatMessageDto.getRedisRoomId());
        if (!Objects.equals(chatMessageDto.getMessageType(), "ENTER")) {
            cmd = chatMessageService.save(chatMessageDto,token);
        }
        redisPublisher.publish(topic, cmd);
        log.info("채팅 메시지");
    }
}
