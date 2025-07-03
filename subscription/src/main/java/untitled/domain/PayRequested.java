package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PayRequested extends AbstractEvent {

    private Long id;
    private Long readerId;

    public PayRequested(Subscribe aggregate) {
        super(aggregate);
        this.readerId = aggregate.getReaderId(); 
    }

    public PayRequested() {
        super();
    }
}
//>>> DDD / Domain Event
