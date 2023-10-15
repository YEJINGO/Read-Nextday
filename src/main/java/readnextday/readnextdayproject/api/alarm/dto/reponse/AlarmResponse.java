package readnextday.readnextdayproject.api.alarm.dto.reponse;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import readnextday.readnextdayproject.entity.AlarmContent;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.entity.Post;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AlarmResponse {
    private LocalDate alarmDate;
    private Long postId;
    private String alarmContent;

    @Builder
    public AlarmResponse(LocalDate alarmDate,Post post, String alarmContent) {
        this.alarmDate = alarmDate;
        this.postId = post.getId();
        this.alarmContent = alarmContent;
    }
}
