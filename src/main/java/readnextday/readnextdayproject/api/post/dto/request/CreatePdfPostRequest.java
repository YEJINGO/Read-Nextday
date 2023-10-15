package readnextday.readnextdayproject.api.post.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@NoArgsConstructor
public class CreatePdfPostRequest {

    private MultipartFile multipartFile;

    private String title;

    private String content;

    private Long categoryId;

    private List<String> tagNames;





}
