package readnextday.readnextdayproject.api.bookmark.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.api.bookmark.dto.response.BookmarkResponse;
import readnextday.readnextdayproject.repository.PostRepository;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.config.auth.LoginMember;
import readnextday.readnextdayproject.entity.Bookmark;
import readnextday.readnextdayproject.entity.Post;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.BookmarkRepository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BookmarkService {

    private final PostRepository postRepository;
    private final BookmarkRepository bookmarkRepository;

    // 1. 북마크 생성 및 해제
    @Transactional
    public Response<Void> postBookmark(Long postId,
                                       LoginMember loginMember) {

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));

        Bookmark findBookmarkPost = bookmarkRepository.findByPostId(findPost.getId());

        if (findBookmarkPost == null) {
            Bookmark bookmark = Bookmark.builder()
                    .member(loginMember.getMember())
                    .post(findPost)
                    .build();
            bookmarkRepository.save(bookmark);
            return Response.success("북마크 설정 완료");
        } else {
            bookmarkRepository.delete(findBookmarkPost);
            return Response.success("북마크 해제 완료");
        }

    }

    // 2. 북마크 전체 조회
    public Response<List<BookmarkResponse>> getAllBookmarkPost(LoginMember loginMember) {
        List<Bookmark> bookmarks = bookmarkRepository.findByMemberId(loginMember.getMember().getId());

        List<BookmarkResponse> bookmarkResponses = bookmarks.stream()
                .map(bookmark -> BookmarkResponse.builder()
                        .id(bookmark.getId())
                        .memberId(bookmark.getMember().getId())
                        .postId(bookmark.getPost().getId())
                        .postTitle(bookmark.getPost().getTitle())
                        .build())
                .collect(Collectors.toList());

        return Response.success("모든 북마크 조회", bookmarkResponses);
    }

}
