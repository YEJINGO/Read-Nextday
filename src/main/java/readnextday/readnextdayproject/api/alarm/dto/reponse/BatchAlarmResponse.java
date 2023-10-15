package readnextday.readnextdayproject.api.alarm.dto.reponse;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BatchAlarmResponse {

    private List<String> BatchAlarmContent;
}
