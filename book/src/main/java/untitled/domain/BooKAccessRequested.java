package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import java.io.File;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class BooKAccessRequested extends AbstractEvent {

    private Long bookId;
    private Long readerId;

    public BooKAccessRequested(Book aggregate) {
        super(aggregate);
    }

    public BooKAccessRequested() {
        super();
    }
}
//>>> DDD / Domain Event
