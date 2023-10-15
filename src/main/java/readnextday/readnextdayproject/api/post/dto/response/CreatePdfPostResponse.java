package readnextday.readnextdayproject.api.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreatePdfPostResponse {

    private String title;
    private String content;
    private String extractTextFromPdf;
    private List<String> tagName;

    @Builder
    public CreatePdfPostResponse(String title, String content, String extractTextFromPdf, List<String> tagName) {
        this.title = title;
        this.content = content;
        this.extractTextFromPdf = extractTextFromPdf;
        this.tagName = tagName;
    }
}
