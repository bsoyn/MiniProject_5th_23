package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.Data;
import untitled.infra.AbstractEvent;

@Data
public class PurchasedCompleted extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;
}
