package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PurchaseFailed extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;
    private String status; // "REQUESTED", "COMPLETED", "FAILED"

    public PurchaseFailed(PurchasedBook book) {
        super(book);

        this.id = book.getId();
        this.readerId = book.getReaderId();
        this.bookId = book.getBookId();
        this.status = book.getStatus();
    }

    public PurchaseFailed() {
        super();
    }
}
//>>> DDD / Domain Event
