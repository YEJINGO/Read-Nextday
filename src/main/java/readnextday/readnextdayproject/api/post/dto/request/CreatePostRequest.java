package readnextday.readnextdayproject.api.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

import static lombok.AccessLevel.PROTECTED;

@Getter
@NoArgsConstructor(access = PROTECTED)
public class CreatePostRequest {

    private String url;
    private String extractTextFromPdf; // 얘 삭제하기
    private String title;
    private int alarmVal;
    private String alarmContent;
    private String content;
    private Long categoryId;
    private List<String> tagNames;
}
