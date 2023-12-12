package readnextday.readnextdayproject.api.fcm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import readnextday.readnextdayproject.api.fcm.dto.FcmTokenRequestDto;
import readnextday.readnextdayproject.api.fcm.service.FcmAlarmService;
import readnextday.readnextdayproject.config.auth.LoginMember;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/fcm-token")
public class FcmAlarmController {

    private final FcmAlarmService fcmAlarmService;

    @PostMapping("/save")
    public void saveNotification(@RequestBody FcmTokenRequestDto fcmTokenRequestDto,
                                 @AuthenticationPrincipal LoginMember loginMember) {
        fcmAlarmService.saveToken(fcmTokenRequestDto, loginMember);
    }
}
