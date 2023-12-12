package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import readnextday.readnextdayproject.entity.Alarm;
import readnextday.readnextdayproject.entity.Member;

import java.time.LocalDate;
import java.util.List;

public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByMemberId(Long id);

    List<Alarm> findByAlarmDate(LocalDate now);

    List<Alarm> findByMemberAndAlarmDate(Member member, LocalDate now);
}
