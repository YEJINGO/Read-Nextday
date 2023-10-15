package readnextday.readnextdayproject.api.category.dto.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CreateCategoryRequest {
    private String categoryName;
    private Long parentId;

    public CreateCategoryRequest(String categoryName) {
        this.categoryName = categoryName;
    }

    public CreateCategoryRequest(String categoryName, Long parentId) {
        this.categoryName = categoryName;
        this.parentId = parentId;
    }
}
