package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class RemainingPointChecked extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Integer point;
    private Long bookId;

    public RemainingPointChecked(Point aggregate) {
        super(aggregate);
        this.readerId = aggregate.getReaderId();
        this.point = aggregate.getPoint();
        this.bookId = aggregate.getBookId();
    }

    public RemainingPointChecked() {
        super();
    }
}
//>>> DDD / Domain Event
