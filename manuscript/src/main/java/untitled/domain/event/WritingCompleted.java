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

    public WritingCompleted(Manuscript aggregate) {
        super(aggregate);
    }

    public WritingCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
