package readnextday.readnextdayproject.api.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import readnextday.readnextdayproject.entity.Category;

import java.util.List;

@Getter
@NoArgsConstructor
public class EditPostRequest {
    private String url;
    private String extractTextFromPdf;
    private String title;
    private int alarmVal;
    private String alarmContent;
    private String content;
    private long categoryId;
    private List<String> tagNames;

    public EditPostRequest(String url, String title, String content) {
        this.url = url;
        this.title = title;
        this.content = content;
    }

    public EditPostRequest(String url, String title, String content, Long parentId) {
        this.url = url;
        this.title = title;
        this.content = content;
    }
}
