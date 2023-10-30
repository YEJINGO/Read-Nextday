package readnextday.readnextdayproject.api.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.api.chat.dto.ChatRoom;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoom> opsHashChatRoom;

    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
    }

    // 1. 채팅방 생성
    public ChatRoom createRoom(String name) {
        ChatRoom chatRoom = ChatRoom.builder()
                .name(name)
                .build();
        opsHashChatRoom.put(CHAT_ROOMS, String.valueOf(chatRoom.getChatRoomId()), chatRoom);
        return chatRoom;
    }

    // 2. 채팅방 전체 조회
    public List<ChatRoom> getAllRooms() {
        return opsHashChatRoom.values(CHAT_ROOMS);
    }

    // 3. 선택된 채팅방 조회하기
    public ChatRoom detailRoom(Long roomId) {
        return opsHashChatRoom.get(CHAT_ROOMS, roomId);
    }

//    private void addUserInfoInSessionAttribute(Message message, SimpMessageHeaderAccessor headerAccessor) {
//        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
//        if (sessionAttributes == null) {
//            throw new IllegalArgumentException("STOMP SessionHeader Error.");
//        }
//        log.info("세션 정보: {}", sessionAttributes);
//        sessionAttributes.put("email", message.getEmail());
//        sessionAttributes.put("username", message.getSender());
//    }
//
//    private void ensureChatRoomExists(Long roomId) {
//        if (!roomRepository.existsByContainer_ContainerId(roomId)) {
//            Container container = containerRepository.findById(roomId)
//                    .orElseThrow(NotFoundContainer::new);
//            Room room = new Room(container);
//            roomRepository.save(room);
//        }
//    }


}




