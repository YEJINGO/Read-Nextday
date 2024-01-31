//package readnextday.readnextdayproject.api.fcm.service;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//import readnextday.readnextdayproject.api.fcm.dto.NotificationRequestDto;
//import readnextday.readnextdayproject.entity.Alarm;
//import readnextday.readnextdayproject.entity.AlarmContent;
//import readnextday.readnextdayproject.entity.FcmToken;
//import readnextday.readnextdayproject.entity.Member;
//import readnextday.readnextdayproject.repository.AlarmRepository;
//import readnextday.readnextdayproject.repository.FcmTokenRepository;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.concurrent.ExecutionException;
//
//@Component
//@RequiredArgsConstructor
//@Slf4j
//public class ScheduledService {
//
//    private final FcmAlarmService fcmAlarmService;
//    private final AlarmRepository alarmRepository;
//    private final FcmTokenRepository fcmTokenRepository;
//
//    /**
//     * 1) AlarmEntity 에서 알람 날짜가 오늘인 것들의 ID 추출
//     * 2) AlarmContent 에서 알람 ID가 일치한 값들 추출
//     * 3) AlarmContent 에서 알림 내용 추출
//     */
//
//    @Scheduled(cron = "0/10 * * * * *")
//    public void scheduledSend() throws ExecutionException, InterruptedException {
//        List<FcmToken> fcmTokens = fcmTokenRepository.findAll();
//
//        if (fcmTokens.isEmpty()) {
//            log.info("FCM 토큰이 비어있어, 알림전송 실패");
//            return;
//        }
//
//        for (FcmToken fcmToken : fcmTokens) {
//            Member member = fcmToken.getMember();
//            List<Alarm> todayAlarms = alarmRepository.findByMemberAndAlarmDate(member, LocalDate.now());
//
//            if (!todayAlarms.isEmpty()) {
//                for (Alarm alarm : todayAlarms) {
//                    AlarmContent alarmContent = alarm.getAlarmContent();
//                    String message = alarmContent.getContent();
//
//                    NotificationRequestDto notificationRequestDto = NotificationRequestDto.builder()
//                            .title("[내일 뭐읽지 알림]")
//                            .message(message)
//                            .token(fcmToken.getToken())
//                            .build();
//
//                    fcmAlarmService.sendNotification(notificationRequestDto);
//                }
//            } else {
//                log.info("설정된 알림이 없어!");
//            }
//        }
//        log.info("웹푸시 보냈어!");
//    }
//
//
//}
