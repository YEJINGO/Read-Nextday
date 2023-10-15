package readnextday.readnextdayproject.api.member.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.entity.Role;

@Getter
@NoArgsConstructor
public class SignupRequest {
    private String email;
    private String slackId;
    private String password;
    private Role role;

    @Builder
    public SignupRequest(String email, String slackId, String password,Role role) {
        this.email = email;
        this.slackId = slackId;
        this.password = password;
        this.role = role;
    }

    public Member toEntity(String password) {
        return Member.builder()
                .email(this.email)
                .slackId(this.slackId)
                .password(password)
                .role(this.role)
                .build();
    }
}
