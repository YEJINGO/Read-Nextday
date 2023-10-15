package readnextday.readnextdayproject.api.post;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import readnextday.readnextdayproject.api.post.dto.request.CreatePdfPostRequest;
import readnextday.readnextdayproject.api.post.dto.request.CreatePostRequest;
import readnextday.readnextdayproject.api.post.dto.request.EditPostRequest;
import readnextday.readnextdayproject.api.post.dto.response.*;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.config.auth.LoginMember;

import java.util.List;

@RestController
@RequestMapping("/api/auth/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 1. 게시글 등록
    @PostMapping
    public Response<CreatePostResponse> createPost(@AuthenticationPrincipal LoginMember loginMember, @RequestBody CreatePostRequest request) {
        return postService.createPost(loginMember, request);
    }

    // 1-2 게시글 등록(PDF)
    @PostMapping(value = "/pdf")
    public Response<CreatePdfPostResponse> createPdfPost(@AuthenticationPrincipal LoginMember loginMember,
                                                         @RequestPart(value = "createPdfPostRequest") CreatePdfPostRequest request,
                                                         @RequestPart(value = "pdfFile") MultipartFile multipartFile) {
        return postService.createPdfPost(loginMember, request,multipartFile);
    }

    // 2. 게시글 수정
    @PatchMapping("/{postId}")
    public Response<EditPostResponse> editPost(@PathVariable Long postId, @AuthenticationPrincipal LoginMember loginMember, @RequestBody EditPostRequest request) {
        return postService.editPost(postId, loginMember, request);
    }

    // 3. 게시글 삭제
    @DeleteMapping("/{postId}")
    public Response<Void> deletePost(@PathVariable Long postId, @AuthenticationPrincipal LoginMember loginMember) {
        return postService.deletePost(postId, loginMember);
    }

    // 4. 게시글 하나 조회
    @GetMapping("/{postId}")
    public Response<GetPostResponse> getPost(@AuthenticationPrincipal LoginMember loginMember, @PathVariable Long postId) {
        return postService.getPost(loginMember, postId);
    }


    // 5. 북마크
    @GetMapping("/{postId}/bookmark")
    public Response<Void> postBookmark(@PathVariable Long postId, @AuthenticationPrincipal LoginMember loginMember) {
        return postService.postBookmark(postId, loginMember);
    }

    @GetMapping("/bookmark")
    public Response<List<BookmarkResponse>> getAllBookmarkPost(@AuthenticationPrincipal LoginMember loginMember) {
        return postService.getAllBookmarkPost(loginMember);
    }
}
