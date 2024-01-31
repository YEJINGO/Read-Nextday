package readnextday.readnextdayproject.api.bookmark.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import readnextday.readnextdayproject.entity.Bookmark;
import readnextday.readnextdayproject.entity.Member;
import readnextday.readnextdayproject.entity.Post;

@Getter
@NoArgsConstructor
public class BookmarkResponse {

    private Long id;
    private Long memberId;
    private Long postId;
    private String postTitle;


    @Builder
    public BookmarkResponse(Long id, Long postId, Long memberId,String postTitle) {
        this.id = id;
        this.memberId = memberId;
        this.postId = postId;
        this.postTitle = postTitle;
    }
}
