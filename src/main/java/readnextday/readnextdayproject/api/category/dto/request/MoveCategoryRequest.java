package readnextday.readnextdayproject.api.category.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MoveCategoryRequest {
    private String currentPath;
    private String movePath;
}
