package readnextday.readnextdayproject.common;

import org.springframework.stereotype.Component;

@Component
public class SlackConstant {

    public static final String POST_CHANNEL = "#게시글상태알림";
    public static final String Alarm_CHANNEL = "#게시글알람";
    public static final String ERROR_CHANNEL = "#내일뭐읽지_에러";
    public static final String LAST_VISITED_AT_IS_NULL_CHANNEL = "#안읽은게시글알람";
}
