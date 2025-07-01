package untitled.domain.BookAccess;

import lombok.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class BookAccessRejected extends AbstractEvent {

    private Long id;
    private Long bookId;
    private Long readerId;

    public BookAccessRejected(BookAccess aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.bookId = aggregate.getBookId();
        this.readerId = aggregate.getReaderId();
    }

    public BookAccessRejected() {
        super();
    }
}
//>>> DDD / Domain Event
