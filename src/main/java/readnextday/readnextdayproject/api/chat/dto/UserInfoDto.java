package readnextday.readnextdayproject.api.chat.dto;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class UserInfoDto implements Serializable {
    private Long id;
    private String name;

    @Builder
    public UserInfoDto(String name) {
        this.name = name;
    }
}


