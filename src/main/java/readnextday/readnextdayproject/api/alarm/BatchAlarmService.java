package readnextday.readnextdayproject.api.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import readnextday.readnextdayproject.entity.Alarm;
import readnextday.readnextdayproject.entity.AlarmContent;
import readnextday.readnextdayproject.entity.Post;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.AlarmContentRepository;
import readnextday.readnextdayproject.repository.AlarmRepository;
import readnextday.readnextdayproject.repository.PostRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BatchAlarmService {

    private final AlarmContentRepository alarmContentRepository;
    private final PostRepository postRepository;
    private final AlarmRepository alarmRepository;

    /**
     * 특정 시간에 알람을 알람 테이블에 저장한다.-> 그 시간에 getAlarm 을 쏜다.
     */

    @Scheduled(cron = "0 0 0 * * *")
    public void batchAlarm() {
        AlarmContent alarmContent = alarmContentRepository.findById(2L).orElseThrow(() -> new GlobalException(ErrorCode.ALARM_CONTENT_NOT_FOUND));
        List<Post> postList = postRepository.findAllByLastVisitedAtNull();
        List<String> alarms = new ArrayList<>();

        for (Post post : postList) {
            if (post.getLastVisitedAt() == null) {

                long alarmVal = ChronoUnit.DAYS.between(post.getCreatedAt().toLocalDate(), LocalDate.now());

                Alarm alarm = Alarm.builder()
                        .alarmDate(post.getCreatedAt().toLocalDate())
                        .alarmVal((int) alarmVal)
                        .member(post.getMember())
                        .post(post)
                        .alarmContent(alarmContent)
                        .build();
                alarmRepository.save(alarm);

                String message;

                String content = alarmContent.getContent();
                content = content.replace("#{postTitle}", post.getTitle());
                content = content.replace("#{date}", String.valueOf(alarmVal));
                message = content;
                alarms.add(message);
            }
        }

    }

}
//    private LoginMember getCurrentUser() {
//        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        if (principal instanceof UserDetails) {
//            return (LoginMember) principal;
//        }
//        return null;
//    }

//    private void sendOrStoreBatchAlarms(List<String> posts) {
//
//        BatchAlarmResponse batchAlarmResponse = new BatchAlarmResponse(posts);
//        System.out.println("배치 알람 기능" + batchAlarmResponse);
//
//    }

