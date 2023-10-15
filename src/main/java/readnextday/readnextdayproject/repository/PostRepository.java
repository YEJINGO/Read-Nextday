package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import readnextday.readnextdayproject.entity.Category;
import readnextday.readnextdayproject.entity.Post;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByIdAndMemberId(Long postId, Long id);

    List<Post> findByMemberId(Long loginMemberId);

    List<Post> findAllByLastVisitedAtNull();
}
