package readnextday.readnextdayproject.exception;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.common.SlackAlarm;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ExceptionController {
    private final SlackAlarm slackAlarm;

    @ExceptionHandler(value = GlobalException.class)
    public ResponseEntity<Response<Void>> handleGlobalExceptionHandler(GlobalException e) {
//        log.error("error occur: {}" , e.getStackTrace());
        log.error("error occur: {}" , e.toString());

        slackAlarm.sendErrorSlackMessage(e);

        return ResponseEntity.status(e.getErrorCode().getStatus())
                .body(Response.error(e.getErrorCode().getMessage()));

    }



}

