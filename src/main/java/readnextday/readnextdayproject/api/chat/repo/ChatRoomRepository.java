package readnextday.readnextdayproject.api.chat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import readnextday.readnextdayproject.api.chat.dto.UserInfoDto;
import readnextday.readnextdayproject.api.chat.entity.ChatRoom;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, String> {

    @Query("select c from ChatRoom c where c.id=:id")
    Optional<ChatRoom> findByChatRoomId(@Param("id") String id);
//    List<ChatRoom> findChatRoomsByCustomer(UserInfoDto customer);
//    List<ChatRoom> findChatRoomsByStore(UserInfoDto store);
}
