package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class CoverCreated extends AbstractEvent {

    private Long manuscriptId;
    private Long bookId;
    private String imageUrl;

    public CoverCreated(BookCover aggregate) {
        super(aggregate);
        this.manuscriptId = aggregate.getManuscriptId();
        this.bookId = aggregate.getBookId();
        this.imageUrl = aggregate.getImageUrl();
    }

    public CoverCreated() {
        super();
    }
}
//>>> DDD / Domain Event
