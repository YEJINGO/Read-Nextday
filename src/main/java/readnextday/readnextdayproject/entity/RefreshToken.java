package readnextday.readnextdayproject.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import readnextday.readnextdayproject.config.auth.LoginMember;

import javax.persistence.Id;

import static readnextday.readnextdayproject.config.jwt.JwtProperties.REFRESH_TOKEN_EXPIRE_TIME_FOR_REDIS;


@RedisHash(value = "refresh")
@Getter
public class RefreshToken {

    @Id
    private Long id;

    private Long loginMemberId;

    @TimeToLive
    private Long expiration = REFRESH_TOKEN_EXPIRE_TIME_FOR_REDIS;

    private String refreshToken;

    @Builder
    public RefreshToken(Long loginMemberId, String refreshToken) {
        this.loginMemberId = loginMemberId;
        this.refreshToken = refreshToken;
    }
}