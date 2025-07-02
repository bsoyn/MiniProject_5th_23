package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseBookRequested extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;
}
