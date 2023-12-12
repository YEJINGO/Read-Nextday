package readnextday.readnextdayproject.api.alarm;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import readnextday.readnextdayproject.api.alarm.dto.reponse.AlarmResponse;
import readnextday.readnextdayproject.api.alarm.dto.request.AlarmRequest;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.config.auth.LoginMember;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth/alarm")
public class AlarmController {

    private final AlarmService alarmService;

    @PostMapping("/{postId}")
    public Response<AlarmResponse> settingAlarm(@AuthenticationPrincipal LoginMember loginMember,
                                                @PathVariable Long postId,
                                                @RequestBody AlarmRequest request) {
        return alarmService.settingAlarm(loginMember, postId, request);
    }

    @GetMapping
    public Response<List<AlarmResponse>> getAlarm(@AuthenticationPrincipal LoginMember loginMember) {
        return alarmService.getAlarm(loginMember);
    }
}

