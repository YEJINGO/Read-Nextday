package readnextday.readnextdayproject.api.post.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AllPostsResponse {
    private Long postId;
    private Long memberId;
    private String url;
    private String title;
    private String content;
    private String extractTextFromPdf;
    private List<String> tags;

    public AllPostsResponse(Long postId, Long memberId, String url, String title, String content, String extractTextFromPdf) {
        this.postId = postId;
        this.memberId = memberId;
        this.url = url;
        this.title = title;
        this.content = content;
        this.extractTextFromPdf = extractTextFromPdf;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }
}
