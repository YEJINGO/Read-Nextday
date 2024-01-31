package readnextday.readnextdayproject.api.post.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import readnextday.readnextdayproject.api.post.dto.request.CreatePostRequest;
import readnextday.readnextdayproject.api.post.dto.request.EditPostRequest;
import readnextday.readnextdayproject.api.post.dto.response.*;
import readnextday.readnextdayproject.api.post.service.PostService;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.config.auth.LoginMember;

import java.util.List;

@RestController
@RequestMapping("/api/auth/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @PostMapping
    public Response<CreatePostResponse<Object>> createPost(@AuthenticationPrincipal LoginMember loginMember,
                                                           @RequestBody CreatePostRequest request) {
        return postService.createPost(loginMember, request);

    }

    @PostMapping("/pdf")
    public Response<CreatePostResponse<Object>> createPdfPost(@AuthenticationPrincipal LoginMember loginMember,
                                                              @RequestPart(value = "createPdfPostRequest") CreatePostRequest createPostRequest,
                                                              @RequestPart(value = "pdfFile", required = false) MultipartFile multipartFile) {
        return postService.createPdfPost(loginMember, createPostRequest, multipartFile);

    }

    // 2. 게시글 수정
    @PatchMapping("/{postId}")
    public Response<EditPostResponse> editPost(@PathVariable Long postId,
                                               @AuthenticationPrincipal LoginMember loginMember,
                                               @RequestBody EditPostRequest request) {
        return postService.editPost(postId, loginMember, request);
    }

    // 3. 게시글 삭제
    @DeleteMapping("/{postId}")
    public Response<Void> deletePost(@PathVariable Long postId,
                                     @AuthenticationPrincipal LoginMember loginMember) {
        return postService.deletePost(postId, loginMember);
    }

    // 4. 게시글 조회
    // 4-1. 게시글 하나 조회
    @GetMapping("/{postId}")
    public Response<GetPostResponse> getPost(@AuthenticationPrincipal LoginMember loginMember,
                                             @PathVariable Long postId) {
        return postService.getPost(loginMember, postId);
    }

    // 4-2. 게시글 전체 조회
    @GetMapping
    public Response<TotalPostsResponse> getAllPost(Pageable pageable,
                                                   @AuthenticationPrincipal LoginMember loginMember) {
        return postService.getAllPost(pageable, loginMember);
    }

    // 게시글 검색하기 (검색 : title, content 에서 검색하기)
    @GetMapping("/search")
    public Response<List<SearchPostResponse>> searchPost(@RequestParam String keyword,
                                                         @PageableDefault(sort = "id", direction = Sort.Direction.DESC) Pageable pageable) {
        return postService.searchPost(keyword, pageable);
    }
}
