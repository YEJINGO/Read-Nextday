package readnextday.readnextdayproject.api.chat.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import readnextday.readnextdayproject.api.chat.dto.request.CreateChatRoomRequest;
import readnextday.readnextdayproject.api.chat.entity.ChatRoom;
import readnextday.readnextdayproject.entity.Member;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ChatRoomDto implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    private Long id;
    private String roomName;
    private String redisRoomId;
    private String sender;
    private String receiver;


    public static ChatRoomDto create(CreateChatRoomRequest messageRequestDto, Member member) {
        ChatRoomDto chatRoomDto = new ChatRoomDto();
        chatRoomDto.roomName = messageRequestDto.getReceiver();
        chatRoomDto.redisRoomId = UUID.randomUUID().toString();
        chatRoomDto.sender = member.getNickname();
        chatRoomDto.receiver = messageRequestDto.getReceiver();

        return chatRoomDto;
    }

    @Builder
    public ChatRoomDto(ChatRoom chatRoom) {
        this.id = chatRoom.getId();
        this.roomName = chatRoom.getRoomName();
        this.redisRoomId = chatRoom.getRedisRoomId();
        this.sender = chatRoom.getSender();
        this.receiver = chatRoom.getReceiver();
    }

}
