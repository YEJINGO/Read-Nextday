package readnextday.readnextdayproject.api.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EditPostResponse {
    private String url;
    private String title;
    private String content;
    private Long parentId;
    private List<String> tagName;


    @Builder
    public EditPostResponse(String url, String title, String content,Long parentId,List<String> tagName) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.parentId = parentId;
        this.tagName = tagName;
    }
}
