package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionValidChecked extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Date subscribeStartDate;
    private Date subscribeEndDate;

    public SubscriptionValidChecked(Subscribe aggregate) {
        super(aggregate);
    }

    public SubscriptionValidChecked() {
        super();
    }
}
//>>> DDD / Domain Event
