package readnextday.readnextdayproject.api.post;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import readnextday.readnextdayproject.entity.Post;
import readnextday.readnextdayproject.exception.ErrorCode;
import readnextday.readnextdayproject.exception.GlobalException;
import readnextday.readnextdayproject.repository.PostRepository;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostHelperClass {

    private final PostRepository postRepository;

    public Post findById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new GlobalException(ErrorCode.POST_NOT_FOUND));
    }
}

