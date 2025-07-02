package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class SubscriptionValidChecked extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;
    private Boolean isSubscribe;

    public SubscriptionValidChecked(Long id, Long readerId, Long bookId, Boolean isSubscribe) {
        super();
        this.id = id;
        this.readerId = readerId;
        this.bookId = bookId;
        this.isSubscribe = isSubscribe;
    }

    public SubscriptionValidChecked() {
        super();
    }
}
//>>> DDD / Domain Event
