package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class BuyRejected extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Integer point;
}
