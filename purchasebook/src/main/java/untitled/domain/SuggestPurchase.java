package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class SuggestPurchase extends AbstractEvent {
    private Long readerId;
    private Long bookId;

    public SuggestPurchase() {
        super();
    }
}
