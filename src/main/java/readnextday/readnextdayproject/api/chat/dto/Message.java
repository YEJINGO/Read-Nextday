package readnextday.readnextdayproject.api.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Message {

    private String chatRoomId;
    private MessageType type;
    private String sender;
    private String message;

    @Builder
    public Message(String chatRoomId, MessageType type, String sender, String message) {
        this.chatRoomId = chatRoomId;
        this.type = type;
        this.sender = sender;
        this.message = message;
    }
}