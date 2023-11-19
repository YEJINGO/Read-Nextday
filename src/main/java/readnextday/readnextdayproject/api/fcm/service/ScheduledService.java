package readnextday.readnextdayproject.api.fcm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import readnextday.readnextdayproject.api.fcm.dto.NotificationRequestDto;
import readnextday.readnextdayproject.entity.FcmToken;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.FcmTokenRepository;
import readnextday.readnextdayproject.repository.MemberRepository;

import java.util.Optional;
import java.util.concurrent.ExecutionException;

import static readnextday.readnextdayproject.exception.ErrorCode.FCM_TOKEN_NOT_FOUND;
import static readnextday.readnextdayproject.exception.ErrorCode.USER_NOT_FOUND;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScheduledService {

    private final FcmAlarmService fcmAlarmService;
    private final MemberRepository memberRepository;
    private final FcmTokenRepository fcmTokenRepository;
    @Scheduled(cron = "0/10 * * * * *")
    public void scheduledSend() throws ExecutionException, InterruptedException {

        Member member = memberRepository.findById(1L).orElseThrow(() -> new GlobalException(USER_NOT_FOUND));
        FcmToken fcmToken = fcmTokenRepository.findByMember(member).orElseThrow(() -> new GlobalException(FCM_TOKEN_NOT_FOUND));
        String token = fcmToken.getToken();

        NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
                .title("read-next-day Alarm")
                .token(token)
                .message("어제 내용 확인할 시간이에요")
                .build();
        fcmAlarmService.sendNotification(notificationRequestDto);
        log.info("보내졌습니다!");
    }
}
