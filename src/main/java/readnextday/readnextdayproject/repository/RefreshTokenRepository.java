package readnextday.readnextdayproject.repository;

import org.springframework.data.repository.CrudRepository;
import readnextday.readnextdayproject.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByRefreshToken(String refreshToken);
}
