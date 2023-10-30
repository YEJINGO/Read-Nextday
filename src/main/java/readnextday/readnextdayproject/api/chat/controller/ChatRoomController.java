package readnextday.readnextdayproject.api.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import readnextday.readnextdayproject.api.chat.dto.ChatRoom;
import readnextday.readnextdayproject.api.chat.service.ChatRoomService;
import readnextday.readnextdayproject.config.jwt.JwtUtils;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;
    private final JwtUtils jwtUtils;

    // 1. 채팅방 생성
    @PostMapping("/room")
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomService.createRoom(name);
    }

    // 2. 채팅방 전체 가져오기
    @GetMapping("/rooms")
    public List<ChatRoom> getAllRooms() {
        return chatRoomService.getAllRooms();
    }

    // 3. 선택된 채팅방 조회하기
    @GetMapping("/rooms/{roomId}")
    public ChatRoom detailRoom(@PathVariable Long roomId) {
        return chatRoomService.detailRoom(roomId);
    }

//    @MessageMapping("/enter/room/{roomId}")
//    @SendTo("/sub/room/{roomId}")
//    public Message enterRoom(@DestinationVariable Long roomId, @Payload Message messageDto, SimpMessageHeaderAccessor headerAccessor) {
//        messageService.enterRoom(roomId, messageDto, headerAccessor);
//        return messageDto;
//    }

//    @GetMapping("/room/{roomId}")
//    @ResponseBody
//    public ChatRoom roomInfo(@PathVariable Long roomId) {
//        return chatRoomRepository.findRoomById(roomId);
//    }

//    @GetMapping("/user")
//    @ResponseBody
//    public LoginInfo getUserInfo() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        String name = auth.getName();
//        return LoginInfo.builder().name(name).token(jwtTokenProvider.generateToken(name)).build();
//    }
}
