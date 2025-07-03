package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

//<<< DDD / Domain Event
@Data
@ToString
public class PurchaseBookRequested extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;
    private Integer price; // 가격도 보내야 함

    public PurchaseBookRequested(PurchasedBook aggregate) {
        super(aggregate);

        this.id = aggregate.getId();
        this.readerId = aggregate.getReaderId();
        this.bookId = aggregate.getBookId();
        this.price = aggregate.getPrice();
    }

    public PurchaseBookRequested() {
        super();
    }
}
//>>> DDD / Domain Event
