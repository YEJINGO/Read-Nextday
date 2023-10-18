package readnextday.readnextdayproject.api.alarmcontent;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import readnextday.readnextdayproject.api.alarm.AlarmService;
import readnextday.readnextdayproject.api.alarmcontent.dto.request.CreateAlarmContentRequest;
import readnextday.readnextdayproject.api.alarmcontent.dto.response.CreateAlarmContentResponse;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.config.auth.LoginMember;

@RestController
@RequestMapping("/api/auth/alarm/content")
@RequiredArgsConstructor
public class AlarmContentController {

    private final AlarmContentService alarmContentService;

    @PostMapping
    public Response<CreateAlarmContentResponse> createAlarmContent(@AuthenticationPrincipal LoginMember loginMember, @RequestBody CreateAlarmContentRequest request) {
        return alarmContentService.createAlarmContent(loginMember, request);

    }

}
