package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import readnextday.readnextdayproject.entity.FcmToken;
import readnextday.readnextdayproject.entity.Member;

import java.util.Optional;
public interface FcmTokenRepository extends JpaRepository<FcmToken, Long> {
    Optional<FcmToken> findByMember(Member member);

    boolean existsByMemberAndToken(Member member, String token);

    Optional<FcmToken> findByMemberAndToken(Member member, String token);
}
