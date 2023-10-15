package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import readnextday.readnextdayproject.entity.Category;

import java.util.Optional;
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> findByIdAndMemberId(Long categoryId, Long id);
}
