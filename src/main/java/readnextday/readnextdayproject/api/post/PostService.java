package readnextday.readnextdayproject.api.post;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import readnextday.readnextdayproject.api.post.dto.request.CreatePdfPostRequest;
import readnextday.readnextdayproject.api.post.dto.request.CreatePostRequest;
import readnextday.readnextdayproject.api.post.dto.request.EditPostRequest;
import readnextday.readnextdayproject.api.post.dto.response.*;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.common.SlackAlarm;
import readnextday.readnextdayproject.config.auth.LoginMember;
import readnextday.readnextdayproject.entity.*;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final PostRepository postRepository;
    private final CategoryRepository categoryRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final PostTagService postTagService;
    private final BookmarkRepository bookmarkRepository;
    private final SlackAlarm slackAlarm;

    @Value("${slack.token}")
    String slackToken;

    @Transactional
    public Response<CreatePostResponse> createPost(LoginMember loginMember, CreatePostRequest request) {


        Post post = Post.urlBuilder()
                .url(request.getUrl())
                .title(request.getTitle())
                .content(request.getContent())
                .member(loginMember.getMember())
                .build();

        extracted(request, post);

        postRepository.save(post);

        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) { // 수정: null 체크 추가
            postTagService.saveTag(post, request.getTagNames());
        }

        List<String> newTagNames = (request.getTagNames() != null && !request.getTagNames().isEmpty()) ? request.getTagNames() : null;

        CreatePostResponse createPostResponse = CreatePostResponse.builder()
                .url(request.getUrl())
                .title(request.getTitle())
                .content(request.getContent())
                .tagName(newTagNames) // tagName 리스트 설정
                .build();

        slackAlarm.sendPostAlarmSlackMessage(post);

        return Response.success("게시물 생성 성공", createPostResponse);

    }

    private void extracted(CreatePostRequest request, Post post) {
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                    new GlobalException(ErrorCode.CATEGORY_NOT_FOUND));
            post.updateCategory(category);
        }
    }

    public Response<CreatePdfPostResponse> createPdfPost(LoginMember loginMember, CreatePdfPostRequest request, MultipartFile multipartFile) {

        try {
            InputStream pdfInputStream = multipartFile.getInputStream();
            String extractedText = extractTextFromPdfFile(pdfInputStream);

            Post post = Post.pdfBuilder()
                    .url(null)
                    .title(request.getTitle())
                    .content(request.getContent())
                    .extractTextFromPdf(extractedText.getBytes())
                    .member(loginMember.getMember())
                    .build();


            if (request.getCategoryId() != null) {
                Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                        new GlobalException(ErrorCode.CATEGORY_NOT_FOUND));
                post.updateCategory(category);
            }

            postRepository.save(post);

            if (request.getTagNames() != null && !request.getTagNames().isEmpty()) { // 수정: null 체크 추가
                postTagService.saveTag(post, request.getTagNames());
            }

            List<String> newTagNames = (request.getTagNames() != null && !request.getTagNames().isEmpty()) ? request.getTagNames() : null;

            CreatePdfPostResponse createPdfPostResponse = CreatePdfPostResponse.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .extractTextFromPdf(extractedText)
                    .tagName(newTagNames)
                    .build();

            slackAlarm.sendPostAlarmSlackMessage(post);


            return Response.success("게시물 생성 성공", createPdfPostResponse);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public String extractTextFromPdfFile(InputStream pdfInputStream) throws IOException {
        try (PDDocument document = PDDocument.load(pdfInputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }


    @Transactional
    public Response<EditPostResponse> editPost(Long postId, LoginMember loginMember, EditPostRequest request) {
        Post findPost = postRepository.findByIdAndMemberId(postId, loginMember.getMember().getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));

        String editUrl = (request.getUrl() != null) ? (request.getUrl().isEmpty() ? "" : request.getUrl()) : findPost.getUrl();
        String editTitle = (request.getTitle() != null) ? (request.getTitle().isEmpty() ? "" : request.getTitle()) : findPost.getTitle();
        String editContent = (request.getContent() != null) ? (request.getContent().isEmpty() ? "" : request.getContent()) : findPost.getContent();

        findPost.update(editUrl, editTitle, editContent);

        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) { // 수정: null 체크 추가

            postTagService.updateTag(findPost, request.getTagNames());
        }

        List<String> newTagNames = (request.getTagNames() != null && !request.getTagNames().isEmpty()) ? request.getTagNames() : null;

        EditPostResponse editPostResponse = EditPostResponse.builder()
                .url(editUrl)
                .title(editTitle)
                .content(editContent)
                .tagName(newTagNames) // tagName 리스트 설정
                .build();

        slackAlarm.sendPostEditAlarmSlackMessage(findPost);

        return Response.success("게시물 수정 성공", editPostResponse);
    }

    public Response<GetPostResponse> getPost(LoginMember loginMember, Long postId) {
        Post findPost = postRepository.findByIdAndMemberId(postId, loginMember.getMember().getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));

        findPost.updateLastVisitedAt(LocalDateTime.now());
        postRepository.save(findPost);

        List<PostTag> postTags = postTagRepository.findByPostId(postId);

        // 태그 ID를 추출한 리스트
        List<Long> tagIds = postTags.stream()
                .map(PostTag::getTagId)
                .distinct()
                .collect(Collectors.toList());

        // Tag 엔티티에서 이름을 가져와서 리스트로 변환
        List<String> tagNames = tagIds.stream()
                .map(tagId -> tagRepository.findById(tagId).map(Tag::getName).orElse(null))
                .filter(tagName -> tagName != null)
                .collect(Collectors.toList());

        GetPostResponse getPostResponse = GetPostResponse.builder()
                .url(findPost.getUrl())
                .title(findPost.getTitle())
                .content(findPost.getContent())
                .tagName(tagNames) // tagName 리스트 설정
                .build();

        return Response.success("게시물 조회", getPostResponse);
    }

    @Transactional
    public Response<Void> deletePost(Long postId, LoginMember loginMember) {
        Post findPost = postRepository.findByIdAndMemberId(postId, loginMember.getMember().getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));

        log.info("findPost" + findPost);
        postRepository.deleteById(findPost.getId());
        log.info("findPost.getId()" + findPost.getId());

        slackAlarm.sendPostDeleteAlarmSlackMessage(findPost);

        return Response.success("게시글 삭제 성공");
    }

    public Response<Void> postBookmark(Long postId, LoginMember loginMember) {

        Post findPost = postRepository.findByIdAndMemberId(postId, loginMember.getMember().getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));

        Bookmark findBookmarkPost = bookmarkRepository.findByPostId(findPost.getId());

        if (findBookmarkPost == null) {
            Bookmark bookmark = Bookmark.builder()
                    .status(true)
                    .member(loginMember.getMember())
                    .post(findPost)
                    .build();
            bookmarkRepository.save(bookmark);
        } else {
            findBookmarkPost.update();
            bookmarkRepository.delete(findBookmarkPost);
        }
        return Response.success("북마크 설정 완료");

    }


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

    public Response<TotalPostsResponse> getAllPost(Pageable pageable, LoginMember loginMember) {
        Page<AllPostsResponse> result = postRepository.findByAllPosts(pageable, loginMember);
        return Response.success("게시물 전체조회 성공", new TotalPostsResponse(result));
    }

}
