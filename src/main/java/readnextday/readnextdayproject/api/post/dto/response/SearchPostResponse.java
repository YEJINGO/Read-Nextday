package readnextday.readnextdayproject.api.post.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class SearchPostResponse {


    private String url;
    private String title;
    private String content;
    private String extractTextFromPdf;
    private List<String> tagName;

}

