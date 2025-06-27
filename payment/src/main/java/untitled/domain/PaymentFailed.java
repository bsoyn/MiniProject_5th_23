package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PaymentFailed extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Integer point;
    private Integer cost;
    private Boolean isCompleted;

    public PaymentFailed(Payment aggregate) {
        super(aggregate);
    }

    public PaymentFailed() {
        super();
    }
}
//>>> DDD / Domain Event
