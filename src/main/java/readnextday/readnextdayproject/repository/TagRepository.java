package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import readnextday.readnextdayproject.entity.Tag;

import java.util.List;
import java.util.Optional;
@Repository
public interface TagRepository extends JpaRepository<Tag,Long> {
    Optional<Tag> findByName(String tag);
}
