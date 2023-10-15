package readnextday.readnextdayproject.api.alarm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SlackService {

    @Value("${slack.token}")
    String slackToken;

}
