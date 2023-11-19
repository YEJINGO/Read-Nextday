package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import readnextday.readnextdayproject.entity.FcmToken;
import readnextday.readnextdayproject.entity.Member;

import java.util.Optional;

public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByMember(Member member);
}
