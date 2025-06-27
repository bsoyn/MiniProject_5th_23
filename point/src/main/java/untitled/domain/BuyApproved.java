package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class BuyApproved extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Integer point;

    public BuyApproved(Point aggregate) {
        super(aggregate);
    }

    public BuyApproved() {
        super();
    }
}
//>>> DDD / Domain Event
