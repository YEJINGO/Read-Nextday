package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import readnextday.readnextdayproject.entity.AlarmContent;

public interface AlarmContentRepository extends JpaRepository<AlarmContent,Long> {
}
