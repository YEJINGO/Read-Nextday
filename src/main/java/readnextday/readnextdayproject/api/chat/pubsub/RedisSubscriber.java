package readnextday.readnextdayproject.api.chat.pubsub;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.api.chat.dto.Message;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber {

    private final ObjectMapper objectMapper;
    private final SimpMessageSendingOperations messagingTemplate;

    /**
     * (1) 메시지 리스터에 메시지가 수신되면 sendMessage 가 수행된다.
     * (2) ChatMessage 객체로 매핑
     * (3) convertAndSend : 채팅방을 구독한 클라이언트에게 메시지 발송
     *
     */
    public void sendMessage(String publishMessage) {
        try {
            // ChatMessage 객채로 맵핑
            Message chatMessage = objectMapper.readValue(publishMessage, Message.class);
            messagingTemplate.convertAndSend("/sub/chat/room/" + chatMessage.getChatRoomId(), chatMessage);
        } catch (Exception e) {
            log.error("Exception {}", e);
        }
    }
}