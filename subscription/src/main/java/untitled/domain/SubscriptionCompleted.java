package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionCompleted extends AbstractEvent {

    private Long id;
    private Long readerId;
    private LocalDate subscribeStartDate;
    private LocalDate subscribeEndDate;

    public SubscriptionCompleted(Subscribe aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.readerId = aggregate.getReaderId();
        this.subscribeStartDate = aggregate.getSubscribeStartDate();
        this.subscribeEndDate = aggregate.getSubscribeEndDate();
    }

    public SubscriptionCompleted() {
        super();
    }
}
//>>> DDD / Domain Event