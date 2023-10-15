package readnextday.readnextdayproject.api.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EditPostRequest {
    private String url;

    private String title;

    private String content;

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
