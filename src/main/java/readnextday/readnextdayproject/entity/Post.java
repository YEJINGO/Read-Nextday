package readnextday.readnextdayproject.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
@SQLDelete(sql = "UPDATE post SET is_deleted = true WHERE POST_ID = ?")
@Where(clause = "is_deleted = false")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POST_ID")
    private Long id;

    private String url;


    private String title;

    private String content;

    @Column(columnDefinition = "TEXT")
    private String extractTextFromPdf;

    private boolean is_deleted = Boolean.FALSE;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CATEGORY_ID")
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostTag> postTag;


    @Builder(builderClassName = "urlBuilder", builderMethodName = "urlBuilder")
    public Post(String url, String title, String content, Member member) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.member = member;
    }

    @Builder(builderClassName = "pdfBuilder", builderMethodName = "pdfBuilder")
    public Post(String url, String title, String content, String extractTextFromPdf, Member member) {
        this.url = url;
        this.title = title;
        this.content = content;
        this.extractTextFromPdf = extractTextFromPdf;
        this.member = member;
    }

    public void update(String url, String title, String content) {
        this.url = url;
        this.title = title;
        this.content = content;
    }

    public void updateCategory(Category category) {
        this.category = category;
    }
}
