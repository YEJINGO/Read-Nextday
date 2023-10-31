package readnextday.readnextdayproject.api.chat.entity;

import lombok.*;
import readnextday.readnextdayproject.entity.BaseEntity;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.api.chat.dto.MessageType;

import javax.persistence.*;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor(access = PROTECTED)
public class ChatMessage extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private MessageType messageType; // 메시지 타입
    private String sender; // 메시지 보낸사람
    private String message; // 메시지
    private String roomId;



    public static ChatMessage createChatMessage(String roomId, String sender, String message,MessageType type) {
        return ChatMessage.builder()
                .roomId(roomId)
                .sender(sender)
                .message(message)
                .messageType(type)
                .build();
    }
}