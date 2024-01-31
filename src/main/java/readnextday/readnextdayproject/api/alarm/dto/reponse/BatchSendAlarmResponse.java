package readnextday.readnextdayproject.api.alarm.dto.reponse;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class BatchSendAlarmResponse {
    private String title;
    private String message;

    @Builder
    public BatchSendAlarmResponse(String title, String message) {
        this.title = title;
        this.message = message;
    }
}
