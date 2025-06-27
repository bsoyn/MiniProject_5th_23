package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class SummaryCreated extends AbstractEvent {

    private Long id;
    private Long bookId;
    private String summary;
    private String category;
    private Float price;

    public SummaryCreated(BookSummary aggregate) {
        super(aggregate);
    }

    public SummaryCreated() {
        super();
    }
}
//>>> DDD / Domain Event
