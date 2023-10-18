package readnextday.readnextdayproject.api.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.entity.Post;
import readnextday.readnextdayproject.entity.PostTag;
import readnextday.readnextdayproject.entity.Tag;
import readnextday.readnextdayproject.repository.PostTagRepository;
import readnextday.readnextdayproject.repository.TagRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class PostTagService {
    private final TagService tagService;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    public void saveTag(Post post, List<String> tagNames) {

        tagNames.stream()
                .map(tag ->
                        tagRepository.findByName(tag)
                                .orElseGet(() -> tagService.save(tag)))
                .forEach(tag -> mapTagToPost(post, tag));
    }

    public void updateTag(Post post, List<String> tagNames) {
        // 현재 게시물(post)와 연결된 모든 태그를 삭제
        List<PostTag> postTags = postTagRepository.findByPostId(post.getId());
        postTagRepository.deleteAll(postTags);

        saveTag(post, tagNames);
//        // 새로운 태그를 추가
//        tagNames.forEach(tagName -> {
//            Optional<Tag> existingTagOptional = tagRepository.findByName(tagName);
//            Tag tag;
//
//            if (existingTagOptional.isPresent()) {
//                // 이미 존재하는 태그를 사용
//                tag = existingTagOptional.get();
//            } else {
//                // 존재하지 않는 태그는 저장하고 사용
//                tag = new Tag(tagName);
//                tagRepository.save(tag);
//            }
//
//            // 게시물과 태그를 연결
//            mapTagToPost(post, tag);

    }


    private Long mapTagToPost(Post post, Tag tag) {
        return postTagRepository.save(new PostTag(post, tag)).getId();
    }

//    public List<PostTag> findTagByPost(Post post) {
//        return PostTagRepository.findAllByPost(post);
//    }
}
