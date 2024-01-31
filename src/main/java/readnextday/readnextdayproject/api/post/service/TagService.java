package readnextday.readnextdayproject.api.post.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import readnextday.readnextdayproject.entity.Tag;
import readnextday.readnextdayproject.repository.TagRepository;

@Service
@RequiredArgsConstructor
public class TagService {
    private final TagRepository tagRepository;

    public Tag save(String tagName) {
        return tagRepository.save(
                Tag.builder()
                        .name(tagName)
                        .build());

    }
}
