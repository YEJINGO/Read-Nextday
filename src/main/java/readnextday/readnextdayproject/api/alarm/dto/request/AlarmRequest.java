package readnextday.readnextdayproject.api.alarm.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import readnextday.readnextdayproject.entity.Alarm;
import readnextday.readnextdayproject.entity.AlarmContent;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.entity.Post;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
public class AlarmRequest {
    private int date;

    public Alarm toEntity(LocalDate alarmDate,Member member, Post post, AlarmContent alarmContent) {
        return Alarm.builder()
                .alarmDate(alarmDate)
                .alarmVal(this.date)
                .member(member)
                .post(post)
                .alarmContent(alarmContent)
                .build();
    }



}
