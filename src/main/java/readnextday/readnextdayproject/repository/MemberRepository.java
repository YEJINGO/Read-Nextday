package readnextday.readnextdayproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import readnextday.readnextdayproject.entity.Member;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByEmailAndNickname(String email, String nickname);
}
