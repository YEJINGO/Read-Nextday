package readnextday.readnextdayproject.api.chat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.api.chat.dto.ChatMessageDto;
import readnextday.readnextdayproject.api.chat.entity.ChatMessage;
import readnextday.readnextdayproject.api.chat.entity.ChatRoom;
import readnextday.readnextdayproject.api.chat.repo.ChatMessageRepository;
import readnextday.readnextdayproject.api.chat.repo.ChatRoomRepository;
import readnextday.readnextdayproject.config.jwt.JwtUtils;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.MemberRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static readnextday.readnextdayproject.exception.ErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final JwtUtils jwtUtils;
    private final ChatMessageRepository chatMessageRepository;
    private final MemberRepository memberRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, ChatMessageDto> redisTemplateMessage;


    /**
     * 1. setValueSerializer: 직렬화
     * 2. rightPush: redis 저장
     * 3. expire: Key값 만료시키기 : 1시간마다 삭제
     */
    public ChatMessageDto save(ChatMessageDto chatMessageDto,String token) {

        Long id = jwtUtils.getId(token);
        Member member = getMember(id);


        ChatRoom chatroom = chatRoomRepository.findByRedisRoomId(chatMessageDto.getRedisRoomId());
        Member member = memberRepository.findByNickname(chatMessageDto.getSender()).orElseThrow(() -> new GlobalException(USER_NOT_FOUND));

        int size = Math.toIntExact(redisTemplate.opsForSet().size(chatroom.getRedisRoomId() + "set"));

        if (size == 2) {
            chatMessageDto.setReadCount(0);
        } else {
            chatMessageDto.setReadCount(1);
        }

        ChatMessage chatMessage = ChatMessage.builder()
                .chatMessageDto(chatMessageDto)
                .chatRoom(chatroom)
                .member(member)
                .build();

        ChatMessageDto chatMessageResponse = ChatMessageDto.builder()
                .chatMessage(chatMessage)
                .build();

        chatMessageRepository.save(chatMessage);
        chatMessageDto.setChatMessageId(chatMessage.getId());
        chatMessageDto.setImageUrl(member.getImageUrl());

        redisTemplateMessage.setValueSerializer(new Jackson2JsonRedisSerializer<>(ChatMessageDto.class));
        redisTemplateMessage.opsForList().rightPush(chatMessageDto.getRedisRoomId(), chatMessageDto);
        redisTemplateMessage.expire(chatMessageDto.getRedisRoomId(), 60, TimeUnit.MINUTES);

        return chatMessageResponse;
    }

    /**
     * 메세지 로드하기
     * 1. Redis에서 메세지 100개를 가져온다.
     * 2. Redis에 저장된 메세지가 없으면, chatMessageRepository에서 redisRoomId와 일치하는 메세지 100개를 가지고 온다.
     * 3. 가져온 메세지를 chatMessageDtos에 담는다.
     *
     * @param roomId
     * @return
     */
    public List<ChatMessageDto> loadMessage(String roomId, Long memberId) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(USER_NOT_FOUND));

        SetOperations<String, Object> setOperations = redisTemplate.opsForSet();
        Long size = setOperations.size(roomId + "set");
        updateReadCount(roomId, member);

        List<ChatMessageDto> chatMessageDtos = new ArrayList<>();
        List<ChatMessageDto> redisMessageList = redisTemplateMessage.opsForList().range(roomId, 0, 99);

        if (redisMessageList == null || redisMessageList.isEmpty() || redisMessageList.size() < 100) {
            List<ChatMessage> dbMessageList = chatMessageRepository.findTop100ByRedisRoomIdOrderByMessageCreatedAtAsc(roomId);

            // 레디스에 값이 일부만 있으면 -> 있는만큼 set, 추가되는건 rightPush
            for (int i = 0; i < redisMessageList.size(); i++) {
                ChatMessageDto chatMessageDto = new ChatMessageDto(dbMessageList.get(i));
                chatMessageDtos.add(chatMessageDto);
                redisTemplateMessage.opsForList().set(roomId, i, chatMessageDto);
            }
            for (int i = redisMessageList.size(); i < dbMessageList.size(); i++) {
                ChatMessageDto chatMessageDto = new ChatMessageDto(dbMessageList.get(i));
                chatMessageDtos.add(chatMessageDto);
                redisTemplateMessage.opsForList().rightPush(roomId, chatMessageDto);
            }

        } else { // 레디스에 값이 충분히 있으면 -> set
            for (int i = 0; i < redisMessageList.size(); i++) {
                if (redisMessageList.get(i).getReadCount() == 1 && size == 2) {
                    redisMessageList.get(i).setReadCount(0);
                    redisTemplateMessage.opsForList().set(roomId, i, redisMessageList.get(i));
                }
            }
            chatMessageDtos.addAll(redisMessageList);
        }
        return chatMessageDtos;
    }


    /**
     * 최신 메세지 가져오기
     * 1. Redis에 값이 있으면 Redis에서 값 가져오기
     * 2. Redis에 값이 없으면 chatMessageRepository에서 값 가져오기
     */
    public ChatMessageDto latestMessage(String roomId) {

        ChatMessageDto latestMessage = redisTemplateMessage.opsForList().index(roomId, -1);
        if (latestMessage == null) {
            ChatMessage dbLatestMessage = chatMessageRepository.findTop1ByRedisRoomIdOrderByMessageCreatedAtDesc(roomId);
            if (dbLatestMessage != null) {
                latestMessage = new ChatMessageDto(dbLatestMessage);
                redisTemplateMessage.opsForList().rightPush(roomId, latestMessage);
            }
        }
        return latestMessage;
    }

    // loadMessage 할 때 내가 안읽은 메세지 readCount 0으로 변경

    public void updateReadCount(String redisRoomId, Member member) {
        String nickName = member.getNickname();
        List<ChatMessage> chatMessages = chatMessageRepository.findAllByRedisRoomIdAndReadCountAndSenderNot(redisRoomId, 1, nickName);

        for (ChatMessage chatMessage : chatMessages) {
            chatMessage.setReadCount(0);
            chatMessageRepository.save(chatMessage);
        }
    }
    private Member getMember(Long id) {
        return memberRepository.findById(id).orElseThrow(() -> new GlobalException(USER_NOT_FOUND));
    }
}
