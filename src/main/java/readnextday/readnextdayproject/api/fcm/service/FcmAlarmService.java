package readnextday.readnextdayproject.api.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.api.fcm.dto.FcmTokenRequestDto;
import readnextday.readnextdayproject.api.fcm.dto.NotificationRequestDto;
import readnextday.readnextdayproject.api.member.MemberHelperClass;
import readnextday.readnextdayproject.config.auth.LoginMember;
import readnextday.readnextdayproject.entity.FcmToken;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.AlarmContentRepository;
import readnextday.readnextdayproject.repository.FcmTokenRepository;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmAlarmService {
    private final MemberHelperClass memberHelperClass;
    private final FcmTokenRepository fcmTokenRepository;
    private final AlarmContentRepository alarmContentRepository;


    /**
     * FcmToken 토큰을 저장한다.
     * 1) 이미 저장된 유저와 토큰이 있다면, 기존 토큰을 새로운 토큰값으로 바꾼다.
     * 2) 저장된 유저가 없다면, 새로운 토큰을 저장한다.
     */

    public void saveToken(FcmTokenRequestDto fcmTokenRequestDto, LoginMember loginMember) {

        Long memberId = loginMember.getMember().getId();
        Member member = memberHelperClass.findById(memberId);

        String token = fcmTokenRequestDto.getToken();
        boolean isExistingToken = fcmTokenRepository.existsByMemberAndToken(member, token);

        if (isExistingToken) {
            updateFcmToken(member, token);
        } else {

            FcmToken fcmToken = FcmToken.builder()
                    .member(member)
                    .token(token)
                    .build();

            fcmTokenRepository.save(fcmToken);
        }

    }

    private void updateFcmToken(Member member, String token) {
        FcmToken fcmToken = fcmTokenRepository.findByMemberAndToken(member, token).orElseThrow(() -> new GlobalException(ErrorCode.FCM_TOKEN_NOT_FOUND));
        fcmToken.updateFcmToken(token);
    }


    public void sendNotification(NotificationRequestDto requestDto) throws ExecutionException, InterruptedException {
        Message message = Message.builder()
                .setWebpushConfig(WebpushConfig.builder()
                        .setNotification(WebpushNotification.builder()
                                .setTitle(requestDto.getTitle())
                                .setBody(requestDto.getMessage())
                                .build())
                        .build())
                .setToken(requestDto.getToken())
                .build();

        String response = FirebaseMessaging.getInstance().sendAsync(message).get();
        log.info(">>>>Send message : " + response);
    }
}
