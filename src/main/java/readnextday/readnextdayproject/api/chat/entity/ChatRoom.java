package readnextday.readnextdayproject.api.chat.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import readnextday.readnextdayproject.api.chat.dto.UserInfoDto;
import readnextday.readnextdayproject.entity.BaseEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Getter
@Entity
@NoArgsConstructor(access = PROTECTED)
public class ChatRoom extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 6494678977089006639L;

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "CHATROOM_ID")
    private String id;

    private String name;

    private UserInfoDto sender;

    @Builder
    public ChatRoom(String name, UserInfoDto sender) {
        this.id = UUID.randomUUID().toString();
        this.name = name;
        this.sender = sender;
    }
}
