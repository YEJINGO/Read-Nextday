package readnextday.readnextdayproject.api.alarm.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import readnextday.readnextdayproject.common.SlackAlarm;
import readnextday.readnextdayproject.entity.Alarm;
import readnextday.readnextdayproject.entity.Post;
import readnextday.readnextdayproject.repository.AlarmContentRepository;
import readnextday.readnextdayproject.repository.AlarmRepository;
import readnextday.readnextdayproject.repository.PostRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BatchAlarmService {

    private final AlarmContentRepository alarmContentRepository;
    private final PostRepository postRepository;
    private final AlarmRepository alarmRepository;
    private final SlackAlarm slackAlarm;

    /**
     * 특정 시간에 알람을 알람 테이블에 저장한다.-> 그 시간에 getAlarm 을 쏜다.
     */

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void batchAlarm() {
        List<Post> postList = postRepository.findAllByLastVisitedAtNull();

        for (Post post : postList) {
            if (post.getLastVisitedAt() == null) {
                slackAlarm.lastVisitedAtIsNull(post);
            }
        }
    }

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void sendAlarm() {
        List<Alarm> alarmList = alarmRepository.findByAlarmDate(LocalDate.now());
        for (Alarm alarm : alarmList) {
            slackAlarm.sendRemindAlarm(alarm);
        }
    }
}


