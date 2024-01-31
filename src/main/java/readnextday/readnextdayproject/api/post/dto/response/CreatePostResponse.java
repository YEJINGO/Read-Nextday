package readnextday.readnextdayproject.api.post.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreatePostResponse<T> {
    private T specificData;
    private String title;
    private String content;
    private String extractTextFromPdf;
    private String alarmContent;
    private List<String> tagName;

    @Builder(builderClassName = "urlBuilder", builderMethodName = "urlBuilder")
    public CreatePostResponse(T specificData, String title, String content, String alarmContent, List<String> tagName) {
        this.specificData = specificData;
        this.title = title;
        this.content = content;
        this.alarmContent = alarmContent;
        this.tagName = tagName;
    }

    @Builder(builderClassName = "pdfBuilder", builderMethodName = "pdfBuilder")
    public CreatePostResponse(T specificData, String title, String content, String extractTextFromPdf, String alarmContent, List<String> tagName) {
        this.specificData = specificData;
        this.title = title;
        this.content = content;
        this.extractTextFromPdf = extractTextFromPdf;
        this.alarmContent = alarmContent;
        this.tagName = tagName;
    }
}
