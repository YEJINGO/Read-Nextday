package readnextday.readnextdayproject.api.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.entity.Post;
import readnextday.readnextdayproject.entity.PostTag;
import readnextday.readnextdayproject.entity.Tag;
import readnextday.readnextdayproject.repository.PostTagRepository;
import readnextday.readnextdayproject.repository.TagRepository;

import java.util.List;

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
                                .orElseGet(()-> tagService.save(tag)))
                .forEach(tag -> mapTagToPost(post,tag));
    }

    private Long mapTagToPost(Post post, Tag tag) {
        return postTagRepository.save(new PostTag(post, tag)).getId();
    }

//    public List<PostTag> findTagByPost(Post post) {
//        return PostTagRepository.findAllByPost(post);
//    }
}
