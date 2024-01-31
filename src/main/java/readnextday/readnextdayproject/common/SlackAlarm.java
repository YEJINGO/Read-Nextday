package readnextday.readnextdayproject.common;

import com.slack.api.Slack;
import com.slack.api.methods.MethodsClient;
import com.slack.api.methods.SlackApiException;
import com.slack.api.methods.request.chat.ChatPostMessageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import readnextday.readnextdayproject.entity.Alarm;
import readnextday.readnextdayproject.entity.Post;
import readnextday.readnextdayproject.exception.GlobalException;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class SlackAlarm {

    @Value("${slack.token}")
    String slackToken;

    private final SlackConstant slackConstant;

    public void sendPostAlarmSlackMessage(Post post) {
        MethodsClient methods = Slack.getInstance().methods(slackToken);
        ChatPostMessageRequest slackRequest = ChatPostMessageRequest.builder()
                .channel(SlackConstant.POST_CHANNEL)
                .text("> 게시글생성" + "\n" + "게시글 제목: " + post.getTitle() + "\n" + "작성자 Slack ID: " + post.getMember().getSlackId())
                .build();

        try {
            methods.chatPostMessage(slackRequest);
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPostEditAlarmSlackMessage(Post post) {
        MethodsClient methods = Slack.getInstance().methods(slackToken);

        ChatPostMessageRequest slackRequest = ChatPostMessageRequest.builder()
                .channel(SlackConstant.POST_CHANNEL)
                .text("> 게시글 수정" + "\n" + "게시글 제목: " + post.getTitle())
                .build();

        try {
            methods.chatPostMessage(slackRequest);
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendPostDeleteAlarmSlackMessage(Post post) {
        MethodsClient methods = Slack.getInstance().methods(slackToken);

        ChatPostMessageRequest slackRequest = ChatPostMessageRequest.builder()
                .channel(SlackConstant.POST_CHANNEL)
                .text("> 게시글 삭제" + "\n" + "게시글 제목: " + post.getTitle())
                .build();

        try {
            methods.chatPostMessage(slackRequest);
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendErrorSlackMessage(GlobalException e) {
        MethodsClient methods = Slack.getInstance().methods(slackToken);

        ChatPostMessageRequest slackRequest = ChatPostMessageRequest.builder()
                .channel(SlackConstant.ERROR_CHANNEL)
                .text("> 에러발생" + "\n" + ":red_circle: 에러코드: " + e.getErrorCode() + "\n에러메시지: " + e.getMessage())
                .build();

        try {
            methods.chatPostMessage(slackRequest);
        } catch (IOException | SlackApiException error) {
            throw new RuntimeException(e);
        }
    }

    public void sendRemindAlarm(Alarm alarm) {
        MethodsClient methods = Slack.getInstance().methods(slackToken);
        ChatPostMessageRequest slackRequest = ChatPostMessageRequest.builder()
                .channel(SlackConstant.Alarm_CHANNEL)
                .text("> 게시글 알람" + "\n" +
                        "게시글 제목: " + alarm.getPost().getTitle() + "\n" +
                        "게시글 알람 내용: " + alarm.getAlarmContent().getContent() + "\n" +
                        "작성자 Slack ID: " + alarm.getMember().getSlackId())
                        .build();

        try {
            methods.chatPostMessage(slackRequest);
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException(e);
        }
    }
    public void lastVisitedAtIsNull(Post post) {
        MethodsClient methods = Slack.getInstance().methods(slackToken);
        ChatPostMessageRequest slackRequest = ChatPostMessageRequest.builder()
                .channel(SlackConstant.LAST_VISITED_AT_IS_NULL_CHANNEL)
                .text("> 읽지 않은 게시글 알람" + "\n" +
                        "게시글 제목: " + post.getTitle()+ "\n" +
                        "작성자 Slack ID: " + post.getMember().getSlackId())
                .build();

        try {
            methods.chatPostMessage(slackRequest);
        } catch (IOException | SlackApiException e) {
            throw new RuntimeException(e);
        }
    }

}


