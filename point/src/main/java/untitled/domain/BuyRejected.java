package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class BuyRejected extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Integer point;
    private Long bookId;

    private Date subscribeStartDate;
    private Date subscribeEndDate;
    private String reason;

    public BuyRejected(Point aggregate) {
        super(aggregate);
    }

    public BuyRejected() {
        super();
    }
}
//>>> DDD / Domain Event
