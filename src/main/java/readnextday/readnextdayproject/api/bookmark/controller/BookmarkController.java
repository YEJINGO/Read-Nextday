package readnextday.readnextdayproject.api.bookmark.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import readnextday.readnextdayproject.api.bookmark.service.BookmarkService;
import readnextday.readnextdayproject.api.bookmark.dto.response.BookmarkResponse;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.config.auth.LoginMember;

import java.util.List;

@RestController
@RequestMapping("/api/a0uth/post/bookmark")
@RequiredArgsConstructor
public class BookmarkController {

    private final BookmarkService bookmarkService;

    // 북마크
    @GetMapping("/{postId}/bookmark")
    public Response<Void> postBookmark(@PathVariable Long postId,
                                       @AuthenticationPrincipal LoginMember loginMember) {
        return bookmarkService.postBookmark(postId, loginMember);
    }

    // 북마크 가져오기
    @GetMapping("/bookmark")
    public Response<List<BookmarkResponse>> getAllBookmarkPost(@AuthenticationPrincipal LoginMember loginMember) {
        return bookmarkService.getAllBookmarkPost(loginMember);
    }
}
