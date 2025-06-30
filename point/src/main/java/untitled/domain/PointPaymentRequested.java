package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
@AllArgsConstructor
public class PointPaymentRequested extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Integer point;
    private String impUid;
    private Integer cost;

    public PointPaymentRequested(Point aggregate) {
        super(aggregate);
        this.readerId = aggregate.getReaderId();
    }

    public PointPaymentRequested() {
        super();
    }
}
//>>> DDD / Domain Event
