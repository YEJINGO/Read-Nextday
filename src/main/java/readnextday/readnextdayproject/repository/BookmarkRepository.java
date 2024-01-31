package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import readnextday.readnextdayproject.entity.Bookmark;
import readnextday.readnextdayproject.entity.Post;

import java.util.List;

public interface BookmarkRepository extends JpaRepository<Bookmark,Long> {

    Bookmark findByPostId(Long id);

    List<Bookmark> findByMemberId(Long id);
}
