package readnextday.readnextdayproject.api.chat.dto.response;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ChatMessageResponse {
    private Long id;
    private String roomName;
    private String sender;
    private String redisRoomId;
    private String receiver;
    private String message;
    private String imageUrl;
    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime messageCreatedAt;


    public ChatMessageResponse(Long id, String roomName, String redisRoomId, String sender, String receiver, String message, String imageUrl, LocalDateTime messageCreatedAt) {
        this.id = id;
        this.roomName = roomName;
        this.redisRoomId = redisRoomId;
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.imageUrl = imageUrl;
        this.messageCreatedAt = messageCreatedAt;

    }
}