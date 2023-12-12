package readnextday.readnextdayproject.api.chat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import readnextday.readnextdayproject.api.chat.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    @Query("select c from ChatRoom c where c.id=:id")
    Optional<ChatRoom> findByChatRoomId(@Param("id") String id);
    ChatRoom findByRedisRoomId(String redisRoomId);
    ChatRoom findBySenderAndReceiver(String nickName, String receiver);

    @Query("SELECT cr FROM ChatRoom cr WHERE (cr.sender = :nickName OR cr.receiver = :nickName)")
    List<ChatRoom> findBySenderOrReceiver(String nickName);

    ChatRoom findByRedisRoomIdAndSenderOrRedisRoomIdAndReceiver(String roomId, String sender, String roomId1, String nickName);



}
