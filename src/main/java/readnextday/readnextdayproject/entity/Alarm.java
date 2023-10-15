package readnextday.readnextdayproject.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Alarm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ALARM_ID")
    private Long id;

    private LocalDate alarmDate;

    private int alarmVal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ALARM_CONTENT_ID")
    private AlarmContent alarmContent;

    @Builder
    public Alarm(LocalDate alarmDate, int alarmVal, Member member, Post post, AlarmContent alarmContent) {
        this.alarmDate = alarmDate;
        this.alarmVal = alarmVal;
        this.member = member;
        this.post = post;
        this.alarmContent = alarmContent;
    }

}

