package readnextday.readnextdayproject.api.chat.pubsub;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.api.chat.dto.ChatMessageDto;
import readnextday.readnextdayproject.api.chat.entity.ChatMessage;

@Service
@RequiredArgsConstructor
public class RedisPublisher {

    private final RedisTemplate<String, Object> redisTemplate;

    public void publish(ChannelTopic topic, ChatMessageDto chatMessageDto) {
        redisTemplate.convertAndSend(topic.getTopic(), chatMessageDto);
    }
}