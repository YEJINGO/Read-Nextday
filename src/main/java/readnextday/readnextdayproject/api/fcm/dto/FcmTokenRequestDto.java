package readnextday.readnextdayproject.api.fcm.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class FcmTokenRequestDto {
    private String token;
}
