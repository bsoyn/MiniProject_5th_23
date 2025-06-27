package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class WritingCompleted extends AbstractEvent {

    private Long id;
    private Long authorId;
    private String title;
    private String content;
    private File image;
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
