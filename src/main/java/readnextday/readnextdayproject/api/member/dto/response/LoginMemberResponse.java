package readnextday.readnextdayproject.api.member.dto.response;

import lombok.Builder;
import lombok.Getter;
import readnextday.readnextdayproject.config.auth.LoginMember;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.entity.Role;

@Getter
public class LoginMemberResponse {

    private Long userId;

    private String email;

    private Role role;

    private String accessToken;

    private String refreshToken;

    @Builder
    public LoginMemberResponse(Member member, String accessToken, String refreshToken) {
        this.userId = member.getId();
        this.email= member.getEmail();
        this.role = member.getRole();
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}