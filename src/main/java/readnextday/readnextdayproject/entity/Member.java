package readnextday.readnextdayproject.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEMBER_ID")
    private Long id;

    @Column(unique = true, length = 100)
    private String email;

    private String password;

    private String slackId;

    private String nickname;

    private String imageUrl;

    @Enumerated(value = EnumType.STRING)
    private Role role;

    @OneToMany(mappedBy = "member", orphanRemoval = true)
    private List<Post> post = new ArrayList<>();

    @Builder
    public Member(Long id, String email, String password, String slackId, String nickname, String imageUrl, Role role) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.slackId = slackId;
        this.nickname = nickname;
        this.imageUrl = imageUrl;
        this.role = role;
    }
}
