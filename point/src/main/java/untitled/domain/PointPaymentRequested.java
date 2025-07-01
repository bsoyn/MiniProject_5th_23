package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

// 책 구매!! 
// 포인트 결제랑 무관!!!

//<<< DDD / Domain Event
@Data
@ToString
public class PointPaymentRequested extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Integer point;
    private Long bookId;

    private Date subscribeStartDate;
    private Date subscribeEndDate;

    public PointPaymentRequested(Point aggregate) {
        super(aggregate);
        this.readerId = aggregate.getReaderId();
    }

    public PointPaymentRequested() {
        super();
    }
}
//>>> DDD / Domain Event
