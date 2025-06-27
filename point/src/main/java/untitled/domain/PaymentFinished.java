package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class PaymentFinished extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Integer point;
    private Integer cost;
    private Boolean isCompleted;
}
