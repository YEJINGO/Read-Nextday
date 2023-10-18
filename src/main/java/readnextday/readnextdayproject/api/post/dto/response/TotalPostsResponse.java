package readnextday.readnextdayproject.api.post.dto.response;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;
@Getter
public class TotalPostsResponse {
    private List<AllPostsResponse> posts;
    private int totalPage;
    private int currentPage;

    public TotalPostsResponse(Page<AllPostsResponse> posts) {
        this.posts = posts.getContent();
        this.totalPage = posts.getTotalPages();
        this.currentPage = posts.getNumber();
    }
}
