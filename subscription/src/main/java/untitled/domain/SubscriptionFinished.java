package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionFinished extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;
    private Boolean isSubscribe;

    public SubscriptionFinished(Subscribe aggregate, Long bookId) {
        super(aggregate);
        this.id = aggregate.getId();
        this.readerId = aggregate.getReaderId();
        this.bookId = bookId;
        this.isSubscribe = false;
    }

    public SubscriptionFinished() {
        super();
    }
}
//>>> DDD / Domain Event
