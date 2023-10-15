package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import readnextday.readnextdayproject.entity.Alarm;

import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByMemberId(Long id);
}
