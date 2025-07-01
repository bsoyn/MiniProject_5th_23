package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class NotPurchaseBookConfirmed extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;

    public NotPurchaseBookConfirmed(Long readerId, Long bookId) {
        super();
        this.readerId = readerId;
        this.bookId = bookId;
    }

    public NotPurchaseBookConfirmed() {
        super();
    }
}
//>>> DDD / Domain Event
