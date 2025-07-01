package untitled.domain.BookAccess;

import lombok.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class BookAccessApproved extends AbstractEvent {

    private Long id;
    private Long bookId;
    private Long readerId;
    private Boolean isPurchased;
    private Boolean isSubscribed;

    public BookAccessApproved(BookAccess aggregate) {
        super(aggregate);
        this.id = aggregate.getId();
        this.bookId = aggregate.getBookId();
        this.readerId = aggregate.getReaderId();
        this.isPurchased = aggregate.getIsPurchased();
        this.isSubscribed = aggregate.getIsSubscribed();
    }

    public BookAccessApproved() {
        super();
    }
}
//>>> DDD / Domain Event
