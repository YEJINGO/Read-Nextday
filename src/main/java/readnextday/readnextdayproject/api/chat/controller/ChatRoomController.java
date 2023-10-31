package readnextday.readnextdayproject.api.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import readnextday.readnextdayproject.api.chat.dto.UserInfoDto;
import readnextday.readnextdayproject.api.chat.entity.ChatMessage;
import readnextday.readnextdayproject.api.chat.entity.ChatRoom;
import readnextday.readnextdayproject.api.chat.service.ChatService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {


    private final ChatService chatService;

    @GetMapping("/rooms")
    public List<ChatRoom> rooms() {
        return chatService.findAllRoom();
    }

    @PostMapping("/room")
    public ChatRoom createRoom(@RequestBody UserInfoDto userInfoDto) {
        return chatService.createChatRoom(userInfoDto);
    }

    @GetMapping("/room/{roomId}")
    public List<ChatMessage> roomChatMessage(@PathVariable String roomId) {
        return chatService.getListResult(roomId);
    }

//    @GetMapping("/user_room")
//    public ListResult<ChatRoom> getRoomsByCustomer(@RequestHeader("X-AUTH-TOKEN") String xAuthToken) {
//        CircuitBreaker circuitBreaker = circuitBreakerFactory.create("circuitbreaker");
//        UserIdDto me = circuitBreaker.run(() -> userServiceClient.getUserId(xAuthToken),
//                throwable -> null);
//        if (me == null) {
//            return responseService.getListResult(new ArrayList<>());
//        }
//        return responseService.getListResult(chatService.getUserEnterRooms(me));
//    }
//
//    @GetMapping("/room/{roomId}/roomInfo")
//    public SingleResult<ChatRoom> roomInfo(@PathVariable String roomId) {
//        return responseService.getSingleResult(chatService.findRoomById(roomId));
//    }
}
