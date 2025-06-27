package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PurchasedCompleted extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;

    public PurchasedCompleted(PurchasedBook aggregate) {
        super(aggregate);
    }

    public PurchasedCompleted() {
        super();
    }
}
//>>> DDD / Domain Event
