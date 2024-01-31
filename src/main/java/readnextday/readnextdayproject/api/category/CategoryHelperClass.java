package readnextday.readnextdayproject.api.category;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import readnextday.readnextdayproject.config.auth.LoginMember;
import readnextday.readnextdayproject.entity.Category;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.CategoryRepository;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CategoryHelperClass {

    private final CategoryRepository categoryRepository;

    public void duplicateCategoryName(String categoryName) {
        if (categoryRepository.findByName(categoryName).isPresent()) {
            throw new GlobalException(ErrorCode.DUPLICATED_CATEGORY_NAME);
        }
    }

    public Category getCategoryById(Long categoryId) {
        return categoryRepository.findById(categoryId)
                .orElseThrow(() -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND));
    }

    public Category findByIdAndMemberId(Long categoryId, Long memberId) {
        return categoryRepository.findByIdAndMemberId(categoryId, memberId)
                .orElseThrow(() -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND));
    }
    public Category getCategoryByIdAndMember(Long categoryId, LoginMember loginMember) {
        Category category = findByIdAndMemberId(categoryId, loginMember.getMember().getId());
        if (category == null) {
            throw new GlobalException(ErrorCode.CATEGORY_NOT_FOUND);
        }
        return category;
    }
}
