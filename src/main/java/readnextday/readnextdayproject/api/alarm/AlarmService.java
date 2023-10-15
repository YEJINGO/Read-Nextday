package readnextday.readnextdayproject.api.alarm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.api.alarm.dto.reponse.AlarmResponse;
import readnextday.readnextdayproject.api.alarm.dto.request.AlarmRequest;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.config.auth.LoginMember;
import readnextday.readnextdayproject.entity.Alarm;
import readnextday.readnextdayproject.entity.AlarmContent;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.entity.Post;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.AlarmContentRepository;
import readnextday.readnextdayproject.repository.AlarmRepository;
import readnextday.readnextdayproject.repository.PostRepository;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@Slf4j
public class AlarmService {

    private final AlarmContentRepository alarmContentRepository;
    private final PostRepository postRepository;
    private final AlarmRepository alarmRepository;

    @Transactional
    public Response<AlarmResponse> settingAlarm(LoginMember loginMember, Long postId, AlarmRequest request) {
        Member member = loginMember.getMember();
        Post post = postRepository.findById(postId).orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));
        AlarmContent alarmContent = alarmContentRepository.findById(1L).orElseThrow(() -> new GlobalException(ErrorCode.ALARM_CONTENT_NOT_FOUND));
        int alarmVal = request.getDate();
        LocalDate alarmDate = LocalDate.now().plusDays(alarmVal);

        Alarm alarm = request.toEntity(alarmDate, member, post, alarmContent);
        alarmRepository.save(alarm);

        AlarmResponse alarmResponse = AlarmResponse.builder()
                .alarmDate(alarmDate)
                .post(post)
                .alarmContent(String.valueOf(alarmContent))
                .build();

        return Response.success("알람 설정이 되었습니다", alarmResponse);
    }

    /**
     * 1. alarmRepository 에서 member_id 와 로그인 한 아이디가 같은 alarm을 찾는다.
     * 2. 그 alarm의 날짜가 LocalDate.now() 와 같을 때, 알람을 전송한다.
     */
    public Response<List<AlarmResponse>> getAlarm(LoginMember loginMember) {

        List<Alarm> alarms = alarmRepository.findByMemberId(loginMember.getMember().getId());
        List<AlarmResponse> alarmResponses = new ArrayList<>();

        for (Alarm alarm : alarms) {
            if (Objects.equals(alarm.getAlarmDate(), LocalDate.now())) {
                String content = String.valueOf(alarm.getAlarmContent());
                content = content.replace("#{postTitle}", alarm.getPost().getTitle()).replace("#{date}", String.valueOf(alarm.getAlarmVal()));

                AlarmResponse alarmResponse = AlarmResponse.builder()
                        .alarmDate(alarm.getAlarmDate())
                        .post(alarm.getPost())
                        .alarmContent(content)
                        .build();

                alarmResponses.add(alarmResponse);
            }
        }
        return Response.success("데이터 추출 성공", alarmResponses);
    }
}
