package readnextday.readnextdayproject.api.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.api.category.dto.request.CreateCategoryRequest;
import readnextday.readnextdayproject.api.category.dto.request.RenameCategoryRequest;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.config.auth.LoginMember;
import readnextday.readnextdayproject.entity.Category;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.CategoryRepository;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryHelperClass categoryHelperClass;

    @Transactional
    public Response<Void> createCategory(LoginMember loginMember, CreateCategoryRequest request) {
        String categoryName = request.getCategoryName();
        categoryHelperClass.duplicateCategoryName(categoryName);

        Category category = buildCategory(loginMember, request);
        updateParentCategory(request.getParentId(), category);

        categoryRepository.save(category);

        return Response.success("카테고리 생성 성공");
    }


    @Transactional
    public Response<Void> renameCategory(LoginMember loginMember, Long categoryId, RenameCategoryRequest request) {

        // 카테고리를 찾아올 때 해당 멤버의 ID와 카테고리 ID를 사용해야 합니다.
        Optional<Category> categoryOptional = categoryRepository.findByIdAndMemberId(categoryId, loginMember.getMember().getId());

        if (categoryOptional.isEmpty()) {
            throw new GlobalException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        if (categoryRepository.findByName(request.getCategoryRename()).isPresent()) {
            throw new GlobalException(ErrorCode.DUPLICATED_CATEGORY_NAME);
        }

        // categoryOptional.get()은 Java의 Optional 클래스에서 사용되는 메서드로, Optional 객체 내부의 값을 추출하는 역할을 한다.
        Category category = categoryOptional.get();
        category.updateName(request.getCategoryRename());

        return Response.success("카테고리 이름 변경 성공");
    }

    @Transactional
    public Response<Void> deleteCategory(LoginMember loginMember, Long categoryId) {

        Category category = categoryHelperClass.getCategoryByIdAndMember(categoryId, loginMember);
        categoryRepository.delete(category);

        return Response.success("카테고리 삭제 성공");
    }

    private Category buildCategory(LoginMember loginMember, CreateCategoryRequest request) {
        return Category.builder()
                .name(request.getCategoryName())
                .member(loginMember.getMember())
                .build();
    }

    private void updateParentCategory(Long parentId, Category category) {
        if (parentId != null) {
            Category parentCategory = categoryHelperClass.getCategoryById(parentId);
            category.updateParent(parentCategory);
        }
    }

}
