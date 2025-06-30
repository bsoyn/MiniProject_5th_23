package untitled.domain.BookAccess;

import lombok.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class NotParchaseBookConfirmed extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;
}