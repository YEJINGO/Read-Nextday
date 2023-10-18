package readnextday.readnextdayproject.repository;

import com.querydsl.core.group.GroupBy;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import readnextday.readnextdayproject.api.post.dto.response.AllPostsResponse;
import readnextday.readnextdayproject.config.auth.LoginMember;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static readnextday.readnextdayproject.entity.QMember.member;
import static readnextday.readnextdayproject.entity.QPost.post;
import static readnextday.readnextdayproject.entity.QPostTag.postTag;
import static readnextday.readnextdayproject.entity.QTag.tag;
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<AllPostsResponse> findByAllPosts(Pageable pageable, LoginMember loginMember) {
        List<AllPostsResponse> findAllPosts = queryFactory
                .select(Projections.constructor(AllPostsResponse.class,
                        post.id,
                        post.member.id,
                        post.url,
                        post.title,
                        post.content,
                        post.extractTextFromPdf))
                .from(post)
                .where(post.member.id.eq(loginMember.getMember().getId()))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        List<Long> findAllPostsId = findAllPosts
                .stream()
                .map(AllPostsResponse::getPostId)
                .collect(Collectors.toList());

        Map<Long, List<String>> tags = queryFactory
                .select(postTag.post.id, postTag.tag.id)
                .from(postTag)
                .leftJoin(tag).on(tag.id.eq(postTag.tag.id))
                .where(postTag.post.id.in(findAllPostsId))
                .orderBy(postTag.post.id.asc())
                .transform(GroupBy.groupBy(postTag.post.id)
                        .as(GroupBy.list(tag.name)));

        findAllPosts.forEach(r ->
                r.setTags(tags.getOrDefault(r.getPostId(), new ArrayList<>())));

        Long count = queryFactory.select(post.count()).from(post).fetchOne();
        return new PageImpl<AllPostsResponse>(findAllPosts, pageable, count);

    }


}
