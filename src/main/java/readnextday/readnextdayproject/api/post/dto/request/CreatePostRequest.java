package readnextday.readnextdayproject.api.post.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreatePostRequest {

    private String url;

    private String title;

    private String content;

    private Long categoryId;

    private List<String> tagNames;
    public CreatePostRequest(String url, String title, String content) {
        this.url = url;
        this.title = title;
        this.content = content;
    }

    public CreatePostRequest(String url, String title, String content, Long parentId) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.categoryId = parentId;
    }
}
