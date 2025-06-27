package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class BookAccessRejected extends AbstractEvent {

    private Long id;
    private Boolean isSubscried;
    private Boolean isPurchased;

    public BookAccessRejected(Book aggregate) {
        super(aggregate);
    }

    public BookAccessRejected() {
        super();
    }
}
//>>> DDD / Domain Event
