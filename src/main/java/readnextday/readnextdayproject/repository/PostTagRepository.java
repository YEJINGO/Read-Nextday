package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import readnextday.readnextdayproject.entity.Post;
import readnextday.readnextdayproject.entity.PostTag;
import readnextday.readnextdayproject.entity.Tag;

import java.util.List;
@Repository
public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    List<PostTag> findByPostId(Long postId);
}
