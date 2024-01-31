package readnextday.readnextdayproject.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import readnextday.readnextdayproject.api.post.dto.response.AllPostsResponse;
import readnextday.readnextdayproject.config.auth.LoginMember;
public interface PostRepositoryCustom {
    Page<AllPostsResponse> findByAllPosts(Pageable pageable, LoginMember loginMember);
}
