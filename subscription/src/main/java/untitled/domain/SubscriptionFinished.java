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
    private Date subscribeStartDate;
    private Date subscribeEndDate;

    public SubscriptionFinished(Subscribe aggregate) {
        super(aggregate);
    }

    public SubscriptionFinished() {
        super();
    }
}
//>>> DDD / Domain Event
