package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import readnextday.readnextdayproject.entity.AlarmContent;

public interface AlarmContentRepository extends JpaRepository<AlarmContent,Long> {
}
