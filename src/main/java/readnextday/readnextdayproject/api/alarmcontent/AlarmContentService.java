package readnextday.readnextdayproject.api.alarmcontent;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.api.alarmcontent.dto.request.CreateAlarmContentRequest;
import readnextday.readnextdayproject.api.alarmcontent.dto.response.CreateAlarmContentResponse;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.config.auth.LoginMember;
import readnextday.readnextdayproject.entity.AlarmContent;
import readnextday.readnextdayproject.entity.Role;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.AlarmContentRepository;

@Service
@RequiredArgsConstructor
public class AlarmContentService {
    private final AlarmContentRepository alarmContentRepository;
    public Response<CreateAlarmContentResponse> createAlarmContent(LoginMember loginMember, CreateAlarmContentRequest request) {
        if (loginMember.getMember().getRole() == Role.USER) {
            throw new GlobalException(ErrorCode.WRONG_APPROACH);
        }

        AlarmContent alarmContent = request.toEntity();
        alarmContentRepository.save(alarmContent);

        CreateAlarmContentResponse createAlarmContentResponse = CreateAlarmContentResponse.builder().alarmContent(request.getAlarmContent()).build();

        return Response.success("알림 내용이 등록되었습니다.", createAlarmContentResponse);

    }
}
