package readnextday.readnextdayproject.api.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class EditPostResponse {
    private String url;
    private String extractTextFromPdf;
    private String title;
    private String content;
    private List<String> tagName;


    @Builder
    public EditPostResponse(String url, String extractTextFromPdf, String title, String content, List<String> tagName) {
        this.url = url;
        this.extractTextFromPdf = extractTextFromPdf;
        this.title = title;
        this.content = content;
        this.tagName = tagName;
    }
}
