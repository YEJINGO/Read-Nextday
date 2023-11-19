package readnextday.readnextdayproject.api.fcm.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.WebpushConfig;
import com.google.firebase.messaging.WebpushNotification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.api.fcm.dto.NotificationRequestDto;
import readnextday.readnextdayproject.config.auth.LoginMember;
import readnextday.readnextdayproject.entity.FcmToken;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.FcmTokenRepository;
import readnextday.readnextdayproject.repository.MemberRepository;

import java.util.concurrent.ExecutionException;

@Slf4j
@Service
@RequiredArgsConstructor
public class FcmAlarmService {
    private final MemberRepository memberRepository;
    private final FcmTokenRepository fcmTokenRepository;

    public void saveToken(String token, LoginMember loginMember) {
        Long memberId = loginMember.getMember().getId();
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new GlobalException(ErrorCode.USER_NOT_FOUND));

        FcmToken fcmToken = FcmToken.builder()
                .member(member)
                .token(token)
                .build();

        fcmTokenRepository.save(fcmToken);
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
