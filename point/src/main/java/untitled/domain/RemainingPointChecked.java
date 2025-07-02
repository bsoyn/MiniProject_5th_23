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
public class RemainingPointChecked extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Integer point;

    private Long bookId;

    private Date subscribeStartDate; // 정기 구독
    private Date subscribeEndDate;

    public RemainingPointChecked(Point aggregate) {
        super(aggregate);
        this.readerId = aggregate.getReaderId();
        this.point = aggregate.getPoint();
    }

    public RemainingPointChecked() {
        super();
    }
}
//>>> DDD / Domain Event
