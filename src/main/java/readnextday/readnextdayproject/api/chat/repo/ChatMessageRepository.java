package readnextday.readnextdayproject.api.chat.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import readnextday.readnextdayproject.api.chat.entity.ChatMessage;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> getChatMessagesByRoomId(String roomId);

}
