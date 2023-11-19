package readnextday.readnextdayproject.api.fcm.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class NotificationRequestDto {

    private String title;
    private String message;
    private String token;

    @Builder
    public NotificationRequestDto(String title, String message, String token) {
        this.title = title;
        this.message = message;
        this.token = token;
    }
}
