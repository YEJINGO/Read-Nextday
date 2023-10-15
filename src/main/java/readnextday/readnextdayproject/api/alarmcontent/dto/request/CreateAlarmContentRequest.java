package readnextday.readnextdayproject.api.alarmcontent.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import readnextday.readnextdayproject.entity.AlarmContent;

@Getter
@NoArgsConstructor
public class CreateAlarmContentRequest {
    private String alarmContent;

    public AlarmContent toEntity() {
        return AlarmContent.builder()
                .content(this.alarmContent)
                .build();
    }
}
