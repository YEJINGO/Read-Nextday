package readnextday.readnextdayproject.api.chat.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import readnextday.readnextdayproject.api.chat.entity.ChatMessage;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class ChatMessageDto {
    private String messageType;
    private Long chatMessageId;
    private String sender;
    private String message;
    private String redisRoomId;
    private int readCount;
    private String imageUrl;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime messageCreatedAt;

    @Builder
    public ChatMessageDto(ChatMessage chatMessage) {
        this.chatMessageId = chatMessage.getId();
        this.sender = chatMessage.getSender();
        this.message = chatMessage.getMessage();
        this.readCount = chatMessage.getReadCount();
        this.imageUrl = chatMessage.getImageUrl();
        this.messageCreatedAt = chatMessage.getMessageCreatedAt();
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}
