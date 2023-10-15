package readnextday.readnextdayproject.api.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreatePostResponse {
    private String url;
    private String title;
    private String content;
    private List<String> tagName;

    @Builder
    public CreatePostResponse(String url, String title, String content,List<String> tagName) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.tagName = tagName;
    }
}
