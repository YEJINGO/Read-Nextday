package readnextday.readnextdayproject.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "CATEGORY_ID")
    private Long id;

    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<Category> children = new ArrayList<>();



    @Builder
    public Category(String name, Member member) {
        this.name = name;
        this.member = member;
    }

    public void updateParent(Category category) {
        this.parent = category;
    }
    public void updateName(String name) {
        this.name = name;
    }

}
