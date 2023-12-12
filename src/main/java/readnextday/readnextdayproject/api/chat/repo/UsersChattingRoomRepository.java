package readnextday.readnextdayproject.api.chat.repo;

import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import readnextday.readnextdayproject.api.chat.entity.UsersChattingRoom;

import java.util.List;

public interface UsersChattingRoomRepository extends JpaRepository<UsersChattingRoom, Long> {

    @Query("select u from UsersChattingRoom u where u.member.id in (:id,:id2)" +
            "group by u.chatRoom having count(u)=2")
    List<UsersChattingRoom> findByChatRoomByMembers(@Param("id") Long memberId, @Param("id2") Long receiverId);
}
