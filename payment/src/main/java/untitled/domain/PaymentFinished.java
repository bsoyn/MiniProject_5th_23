package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PaymentFinished extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Integer point;
    private Integer cost;
    private Boolean isCompleted;

    public PaymentFinished(Payment aggregate) {
        super(aggregate);
    }

    public PaymentFinished() {
        super();
    }
}
//>>> DDD / Domain Event
