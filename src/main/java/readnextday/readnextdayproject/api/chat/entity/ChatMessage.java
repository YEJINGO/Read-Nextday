package readnextday.readnextdayproject.api.chat.entity;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import readnextday.readnextdayproject.entity.Member;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private String redisRoomId;
    private String message;
    private int readCount;

    @JsonSerialize(using = LocalDateTimeSerializer.class)
    @JsonDeserialize(using = LocalDateTimeDeserializer.class)
    private LocalDateTime messageCreatedAt;

    @ManyToOne
    @JoinColumn(name = "email")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "chat_room_id")
    private ChatRoom chatRoom;

    @Builder
    public ChatMessage(ChatRoom chatRoom, String message, String redisRoomId, int readCount, Member member) {
        super();
        this.member = member;
        this.chatRoom = chatRoom;
        this.redisRoomId = redisRoomId;
        this.message = message;
        this.readCount = readCount;
        this.messageCreatedAt = LocalDateTime.now();
    }
}
