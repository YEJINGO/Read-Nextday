package readnextday.readnextdayproject.api.post.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import readnextday.readnextdayproject.api.post.dto.request.CreatePostRequest;
import readnextday.readnextdayproject.api.post.dto.request.EditPostRequest;
import readnextday.readnextdayproject.api.post.dto.response.*;
import readnextday.readnextdayproject.repository.PostRepository;
import readnextday.readnextdayproject.common.Response;
import readnextday.readnextdayproject.common.SlackAlarm;
import readnextday.readnextdayproject.config.auth.LoginMember;
import readnextday.readnextdayproject.config.s3.S3Service;
import readnextday.readnextdayproject.entity.*;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.*;

import javax.transaction.Transactional;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final AlarmContentRepository alarmContentRepository;
    private final CategoryRepository categoryRepository;
    private final PdfFileRepository pdfFileRepository;
    private final PostTagRepository postTagRepository;
    private final AlarmRepository alarmRepository;
    private final PostRepository postRepository;
    private final PostTagService postTagService;
    private final TagRepository tagRepository;
    private final SlackAlarm slackAlarm;
    private final S3Service s3Service;

    /**
     * 게시물 생성하기
     * 1. url 게시물 또는 url,pdf 설정이 되어 있지 않는 게시물 생성 -> createPost
     * 2. pdf 설정이 되어 있는 게시물 생성 -> createPdfPost
     */
    @Transactional
    public Response<CreatePostResponse<Object>> createPost(LoginMember loginMember,
                                                           CreatePostRequest request) {

        Post post;

        if (request.getUrl() != null && !request.getUrl().isEmpty()) {
            post = Post.urlBuilder()
                    .url(request.getUrl())
                    .title(request.getTitle())
                    .content(request.getContent())
                    .member(loginMember.getMember())
                    .build();
            postRepository.save(post);
        } else {
            post = Post.noUrlAndPdf()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .member(loginMember.getMember())
                    .build();

            postRepository.save(post);
        }

        saveCategory(request, post);
        AlarmContent alarmContent = saveAlarmContent(loginMember, request, post);
        saveTags(request, post);

        List<String> newTagNames = (request.getTagNames() != null && !request.getTagNames().isEmpty()) ? request.getTagNames() : null;

        CreatePostResponse<Object> createPostResponse = CreatePostResponse.urlBuilder()
                .specificData(request.getUrl())
                .title(request.getTitle())
                .content(request.getContent())
                .alarmContent(alarmContent.getContent())
                .tagName(newTagNames)
                .build();

        slackAlarm.sendPostAlarmSlackMessage(post);

        return Response.success("게시물 생성 성공", createPostResponse);
    }

    public Response<CreatePostResponse<Object>> createPdfPost(LoginMember loginMember,
                                                              CreatePostRequest createPostRequest,
                                                              MultipartFile multipartFile) {

        try {
            InputStream pdfInputStream = multipartFile.getInputStream();
            String extractedText = extractTextFromPdfFile(pdfInputStream);

            String localDate = LocalDate.now().toString();
            String fileName = s3Service.uploadFileToS3(multipartFile, localDate);

            Post post = Post.pdfBuilder()
                    .url(null)
                    .title(createPostRequest.getTitle())
                    .content(createPostRequest.getContent())
                    .extractTextFromPdf(extractedText)
                    .member(loginMember.getMember())
                    .build();
            postRepository.save(post);

            PdfFile pdfFile = PdfFile.builder()
                    .fileName(fileName)
                    .post(post)
                    .build();
            pdfFileRepository.save(pdfFile);

            saveCategory(createPostRequest, post);
            AlarmContent alarmContent = saveAlarmContent(loginMember, createPostRequest, post);
            saveTags(createPostRequest, post);
            List<String> newTagNames = (createPostRequest.getTagNames() != null && !createPostRequest.getTagNames().isEmpty()) ? createPostRequest.getTagNames() : null;

            CreatePostResponse<Object> createPostResponse = CreatePostResponse.pdfBuilder()
                    .specificData(fileName)
                    .title(createPostRequest.getTitle())
                    .content(createPostRequest.getContent())
                    .extractTextFromPdf(extractedText)
                    .alarmContent(alarmContent.getContent())
                    .tagName(newTagNames)
                    .build();

            slackAlarm.sendPostAlarmSlackMessage(post);
            return Response.success("게시물 생성 성공", createPostResponse);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 게시물 수정하기
     */
    @Transactional
    public Response<EditPostResponse> editPost(Long postId,
                                               LoginMember loginMember,
                                               EditPostRequest request) {

        Post findPost = postRepository.findByIdAndMemberId(postId, loginMember.getMember().getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new GlobalException(ErrorCode.CATEGORY_NOT_FOUND));


        String editUrl = StringUtils.defaultIfBlank(request.getUrl(), findPost.getUrl());
        String editExtractTextFromPdf = StringUtils.defaultIfBlank(request.getExtractTextFromPdf(), findPost.getExtractTextFromPdf());
        Category editCategory = Optional.of(category).orElse(findPost.getCategory());
        String editTitle = StringUtils.defaultIfBlank(request.getTitle(), findPost.getTitle());
        String editContent = StringUtils.defaultIfBlank(request.getContent(), findPost.getContent());

        findPost.update(editUrl, editExtractTextFromPdf, editCategory, editTitle, editContent);

        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) { // 수정: null 체크 추가
            postTagService.updateTag(findPost, request.getTagNames());
        }

        List<String> newTagNames = (request.getTagNames() != null && !request.getTagNames().isEmpty()) ? request.getTagNames() : null;

        EditPostResponse editPostResponse = EditPostResponse.builder()
                .url(editUrl)
                .extractTextFromPdf(editExtractTextFromPdf)
                .title(editTitle)
                .content(editContent)
                .tagName(newTagNames) // return 할 tagName 리스트
                .build();

        slackAlarm.sendPostEditAlarmSlackMessage(findPost);

        return Response.success("게시물 수정 성공", editPostResponse);
    }

    /**
     * 게시글 조회
     */

    public Response<GetPostResponse> getPost(LoginMember loginMember,
                                             Long postId) {
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

    /**
     * 게시물 전체 조회
     */
    @Transactional
    public Response<TotalPostsResponse> getAllPost(Pageable pageable, LoginMember loginMember) {
        Page<AllPostsResponse> result = postRepository.findByAllPosts(pageable, loginMember);
        return Response.success("게시물 전체조회 성공", new TotalPostsResponse(result));
    }

    @Transactional
    public Response<Void> deletePost(Long postId,
                                     LoginMember loginMember) {
        Post findPost = postRepository.findByIdAndMemberId(postId, loginMember.getMember().getId())
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));

        log.info("findPost" + findPost);
        postRepository.deleteById(findPost.getId());
        log.info("findPost.getId()" + findPost.getId());

        slackAlarm.sendPostDeleteAlarmSlackMessage(findPost);

        return Response.success("게시글 삭제 성공");
    }


    //게시글 검색하기 (검색 : title, content 에서 검색)
    public Response<List<SearchPostResponse>> searchPost(String keyword,
                                                         Pageable pageable) {
        List<Post> searchPost = postRepository.findByTitleContainingOrContentContainingOrExtractTextFromPdfContaining(keyword, keyword, keyword, pageable);
        List<SearchPostResponse> responseList = new ArrayList<>();

        List<Long> searchPostIds = searchPost.stream()
                .map(Post::getId)
                .collect(Collectors.toList());

        // postTagRepository.findByPostIdIn(searchPostIds)로 변경
        // in을 사용하여 여러 ID를 받을 수 있음
        List<PostTag> postTags = postTagRepository.findByPostIdIn(searchPostIds);

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

        for (Post post : searchPost) {
            String content = post.getContent() != null ? post.getContent() : post.getExtractTextFromPdf();

            SearchPostResponse.SearchPostResponseBuilder responseBuilder = SearchPostResponse.builder()
                    .url(post.getUrl())
                    .title(post.getTitle())
                    .content(content)
                    .tagName(tagNames);

            if (post.getExtractTextFromPdf() != null) {
                responseBuilder.extractTextFromPdf(post.getExtractTextFromPdf());
            }

            responseList.add(responseBuilder.build());
        }
        return Response.success("게시글 검색 성공", responseList);
    }

    private void saveTags(CreatePostRequest request, Post post) {
        if (request.getTagNames() != null && !request.getTagNames().isEmpty()) {
            postTagService.saveTag(post, request.getTagNames());
        }
    }

    private AlarmContent saveAlarmContent(LoginMember loginMember,
                                          CreatePostRequest request,
                                          Post post) {
        if (!request.getAlarmContent().isEmpty()) {
            AlarmContent alarmContent = AlarmContent.builder()
                    .content(request.getAlarmContent())
                    .build();
            alarmContentRepository.save(alarmContent);
            saveAlarm(loginMember, request, post, alarmContent);
            return alarmContent;
        } else {
            return AlarmContent.builder().content("").build();
        }
    }

    private void saveAlarm(LoginMember loginMember,
                           CreatePostRequest request,
                           Post post,
                           AlarmContent alarmContent) {
        Alarm alarm = Alarm.builder()
                .alarmDate(LocalDate.now().plusDays(request.getAlarmVal()))
                .alarmVal(request.getAlarmVal())
                .member(loginMember.getMember())
                .post(post)
                .alarmContent(alarmContent)
                .build();
        alarmRepository.save(alarm);
    }

    private void saveCategory(CreatePostRequest request,
                              Post post) {
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId()).orElseThrow(() ->
                    new GlobalException(ErrorCode.CATEGORY_NOT_FOUND));
            post.updateCategory(category);
        }
    }


    public String extractTextFromPdfFile(InputStream pdfInputStream) throws IOException {
        try (PDDocument document = PDDocument.load(pdfInputStream)) {
            PDFTextStripper stripper = new PDFTextStripper();
            return stripper.getText(document);
        }
    }
}
