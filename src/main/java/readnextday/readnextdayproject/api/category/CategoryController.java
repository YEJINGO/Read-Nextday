package readnextday.readnextdayproject.api.category;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import readnextday.readnextdayproject.api.category.dto.request.CreateCategoryRequest;
import readnextday.readnextdayproject.api.category.dto.request.MoveCategoryRequest;
import readnextday.readnextdayproject.api.category.dto.request.RenameCategoryRequest;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.config.auth.LoginMember;


@RestController
@RequestMapping("/api/auth/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    // 1. 카테고리 생성
    @PostMapping
    public Response<Void> createCategory(@AuthenticationPrincipal LoginMember loginMember,
                                         @RequestBody CreateCategoryRequest request) {
        return categoryService.createCategory(loginMember, request);
    }

    // 2. 카테고리 이름 수정
    @PutMapping("/rename/{categoryId}")
    public Response<Void> renameCategory(@AuthenticationPrincipal LoginMember loginMember,
                                         @PathVariable Long categoryId,
                                         @RequestBody RenameCategoryRequest request) {
        return categoryService.renameCategory(loginMember, categoryId, request);
    }

    // 3. 카테고리 삭제
    @DeleteMapping("/{categoryId}")
    public Response<Void> deleteCategory(@AuthenticationPrincipal LoginMember loginMember,
                                         @PathVariable Long categoryId) {
        return categoryService.deleteCategory(loginMember, categoryId);
    }
}
