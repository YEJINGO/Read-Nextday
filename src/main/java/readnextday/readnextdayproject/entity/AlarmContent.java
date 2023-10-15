package readnextday.readnextdayproject.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AlarmContent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALARM_CONTENT_ID")
    private Long id;

    private String content;

    @Builder
    public AlarmContent(String content) {
        this.content = content;
    }
}
