package untitled.domain.BookAccess;

import lombok.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class BooKAccessRequested extends AbstractEvent {

    private Long id;
    private Long bookId;
    private Long readerId;

    public BooKAccessRequested(BookAccess aggregate) {
        super(aggregate);
    }

    public BooKAccessRequested() {
        super();
    }
}
//>>> DDD / Domain Event
