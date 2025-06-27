package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class NotParchaseBookConfirmed extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;

    public NotParchaseBookConfirmed(PurchasedBook aggregate) {
        super(aggregate);
    }

    public NotParchaseBookConfirmed() {
        super();
    }
}
//>>> DDD / Domain Event
