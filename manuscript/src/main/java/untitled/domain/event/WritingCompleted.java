package untitled.domain.event;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.infra.AbstractEvent;
import untitled.domain.aggregate.Manuscript;

import java.io.*; 
//<<< DDD / Domain Event
@Data
@ToString
public class WritingCompleted extends AbstractEvent {

    private Long id;
    private Long manuscriptId; 
    private Long authorId;
    private String title;
    private String contents;
    private String imageUrl;
    private String summary;
    private String category;
    private Integer price;
    private String authorName; // 필명 필드 추가

    public WritingCompleted(Manuscript aggregate, String penName) {
        super(aggregate);
        this.authorName = penName; 
    }

    public WritingCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
