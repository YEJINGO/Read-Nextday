package readnextday.readnextdayproject.api.chat.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import readnextday.readnextdayproject.api.chat.dto.request.CreateChatRoomRequest;
import readnextday.readnextdayproject.api.chat.dto.response.ChatMessageResponse;
import readnextday.readnextdayproject.api.chat.dto.response.ChatRoomResponse;
import readnextday.readnextdayproject.api.chat.dto.response.SelectedChatRoomResponse;
import readnextday.readnextdayproject.api.chat.service.ChatRoomService;
import readnextday.readnextdayproject.config.auth.LoginMember;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatRoomController {


    private final ChatRoomService chatService;

    /**
     * 채팅방 생성
     */
    @PostMapping("/rooms")
    public ChatRoomResponse createRoom(@RequestBody CreateChatRoomRequest chatMessageRequest,
                                       @AuthenticationPrincipal LoginMember loginMember) {
        return chatService.createChatRoom(chatMessageRequest, loginMember);
    }


    /**
     * 사용자 관련 모든 채팅방 조회
     */
    @GetMapping("/rooms")
    public List<ChatMessageResponse> findAllRoomByUser(@AuthenticationPrincipal LoginMember loginMember) {
        return chatService.findAllRoomByUser(loginMember);
    }

    /**
     * 사용자 관련 선택된 채팅방 조회
     */
    @GetMapping("/rooms/{roomId}")
    public SelectedChatRoomResponse findRoom(@PathVariable String roomId,
                                             @AuthenticationPrincipal LoginMember loginMember) {
        return chatService.findRoom(roomId, loginMember);
    }
}