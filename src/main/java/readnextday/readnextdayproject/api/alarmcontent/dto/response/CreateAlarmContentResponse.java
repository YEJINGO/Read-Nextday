package readnextday.readnextdayproject.api.alarmcontent.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateAlarmContentResponse {
    private String alarmContent;

    @Builder
    public CreateAlarmContentResponse(String alarmContent) {
        this.alarmContent = alarmContent;
    }
}
