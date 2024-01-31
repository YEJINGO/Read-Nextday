package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import readnextday.readnextdayproject.entity.Alarm;
import readnextday.readnextdayproject.entity.Member;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
public interface AlarmRepository extends JpaRepository<Alarm, Long> {
    List<Alarm> findByMemberId(Long id);

    List<Alarm> findByAlarmDate(LocalDate now);

    List<Alarm> findByMemberAndAlarmDate(Member member, LocalDate now);

    Optional<Alarm> findByPostIdAndMemberId(Long postId, Long id);
}
