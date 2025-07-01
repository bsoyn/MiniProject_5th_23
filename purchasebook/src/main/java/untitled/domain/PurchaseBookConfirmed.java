package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PurchaseBookConfirmed extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;

    public PurchaseBookConfirmed(PurchasedBook aggregate) {
        super(aggregate);

        this.id = aggregate.getId();
        this.readerId = aggregate.getReaderId();
        this.bookId = aggregate.getBookId();
    }

    public PurchaseBookConfirmed() {
        super();
    }
}
//>>> DDD / Domain Event
