package untitled.domain.BookAccess;

import lombok.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class PurchaseBookConfirmed extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;
}
