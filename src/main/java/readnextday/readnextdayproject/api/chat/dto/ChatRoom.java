package readnextday.readnextdayproject.api.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoom implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private String chatRoomId;
    private String name;

    @Builder
    public static ChatRoom create(String name) {
        ChatRoom chatRoom = new ChatRoom();
        chatRoom.chatRoomId = UUID.randomUUID().toString();
        chatRoom.name = name;
        return chatRoom;
    }

}
