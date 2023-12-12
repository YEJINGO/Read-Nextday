package readnextday.readnextdayproject.api.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.api.chat.dto.ChatMessageDto;
import readnextday.readnextdayproject.api.chat.dto.ChatRoomDto;
import readnextday.readnextdayproject.api.chat.dto.request.CreateChatRoomRequest;
import readnextday.readnextdayproject.api.chat.dto.response.ChatMessageResponse;
import readnextday.readnextdayproject.api.chat.dto.response.ChatRoomResponse;
import readnextday.readnextdayproject.api.chat.dto.response.SelectedChatRoomResponse;
import readnextday.readnextdayproject.api.chat.entity.ChatRoom;
import readnextday.readnextdayproject.api.chat.entity.UsersChattingRoom;
import readnextday.readnextdayproject.api.chat.pubsub.RedisSubscriber;
import readnextday.readnextdayproject.api.chat.repo.ChatMessageRepository;
import readnextday.readnextdayproject.api.chat.repo.ChatRoomRepository;
import readnextday.readnextdayproject.api.chat.repo.UsersChattingRoomRepository;
import readnextday.readnextdayproject.config.auth.LoginMember;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.MemberRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static readnextday.readnextdayproject.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatRoomService {


    private final RedisSubscriber redisSubscriber;
    private final RedisMessageListenerContainer redisMessageListener;
    private final UsersChattingRoomRepository usersChattingRoomRepository;
    private static final String CHAT_ROOMS = "CHAT_ROOM";
    private final RedisTemplate<String, Object> redisTemplate;
    private HashOperations<String, String, ChatRoomDto> opsHashChatRoom;
    private Map<String, ChannelTopic> topics;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageService chatMessageService;


    @PostConstruct
    private void init() {
        opsHashChatRoom = redisTemplate.opsForHash();
        topics = new HashMap<>();
    }

    /**
     * 채팅방 입장 : redis에 topic을 만들고 pub/sub 통신을 하기 위해 리스너를 설정한다.
     * 여기서 topic은 채팅방의 redisRoomId를 의미한다.
     */
    public void enterChatRoom(String roomId) {
        ChannelTopic topic = topics.get(roomId);
        if (topic == null)
            topic = new ChannelTopic(roomId);

        redisMessageListener.addMessageListener(redisSubscriber, topic);
        topics.put(roomId, topic);
    }

    public ChannelTopic getTopic(String roomId) {
        return topics.get(roomId);
    }

    /**
     * 채팅방 생성 : 서버간 채팅방 공유를 위해 redis hash에 저장한다.
     */
    public ChatRoomResponse createChatRoom(CreateChatRoomRequest chatMessageRequest, LoginMember loginMember) {
        Member member = getMember(loginMember.getMember().getId());
        Member receiver = getReceiver(chatMessageRequest);

        List<UsersChattingRoom> usersChattingRoom = usersChattingRoomRepository.findByChatRoomByMembers(member.getId(), receiver.getId());
        if (usersChattingRoom.size() == 2) {
            return new ChatRoomResponse(usersChattingRoom.get(0).getChatRoom().getRedisRoomId());
        } else {
            ChatRoom savedChatRoom = new ChatRoom();
            chatRoomRepository.save(savedChatRoom);

            usersChattingRoomRepository.save(new UsersChattingRoom(member, savedChatRoom));
            usersChattingRoomRepository.save(new UsersChattingRoom(receiver, savedChatRoom));

            return new ChatRoomResponse(savedChatRoom, member, receiver);
        }
    }

    private Member getReceiver(CreateChatRoomRequest chatMessageRequest) {
        return memberRepository.findByNickname(chatMessageRequest.getReceiver()).orElseThrow(() -> new GlobalException(USER_NOT_FOUND));
    }

    public List<ChatMessageResponse> findAllRoomByUser(Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
        List<ChatRoom> chatRooms = chatRoomRepository.findBySenderOrReceiver(member.getNickname());

        List<ChatMessageResponse> chatRoomMessageResponses = new ArrayList<>();

        for (ChatRoom chatRoom : chatRooms) {
            ChatMessageDto latestMsg = chatMessageService.latestMessage(chatRoom.getRedisRoomId());
            String msg = (latestMsg != null) ? latestMsg.getMessage() : "";
            LocalDateTime messageCreatedAt = (latestMsg != null) ? latestMsg.getMessageCreatedAt() : null;

            if (member.getNickname().equals(chatRoom.getSender())) {
                ChatMessageResponse chatMessageResponseDto = new ChatMessageResponse(
                        chatRoom.getId(),
                        chatRoom.getReceiver(), // roomName
                        chatRoom.getRedisRoomId(),
                        chatRoom.getSender(),
                        chatRoom.getReceiver(),
                        msg,
                        chatRoom.getImageUrl(),
                        messageCreatedAt
                );
                chatRoomMessageResponses.add(chatMessageResponseDto);
            } else if (member.getNickname().equals(chatRoom.getReceiver())) {
                ChatMessageResponse chatMessageResponseDto = new ChatMessageResponse(
                        chatRoom.getId(),
                        chatRoom.getSender(),        // roomName
                        chatRoom.getRedisRoomId(),
                        chatRoom.getSender(), //sender
                        chatRoom.getReceiver(),
                        msg,
                        chatRoom.getImageUrl(),
                        messageCreatedAt
                );
                chatRoomMessageResponses.add(chatMessageResponseDto);
            } else {
                return chatRoomMessageResponses;
            }
        }
        return chatRoomMessageResponses;
    }


    public SelectedChatRoomResponse findRoom(String roomId, Long memberId) {

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findByRedisRoomIdAndSenderOrRedisRoomIdAndReceiver(roomId, member.getNickname(), roomId, member.getNickname());
        List<ChatMessageDto> chatMessageDtos = chatMessageService.loadMessage(roomId, memberId);

        if (chatRoom == null) {
            throw new IllegalArgumentException("채팅방이 없습니다.");
        }
        return new SelectedChatRoomResponse(chatRoom, chatMessageDtos);
    }

    private Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new GlobalException(USER_NOT_FOUND));
    }
}