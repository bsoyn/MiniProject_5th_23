package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class PayRequested extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Date subscribeStartDate;
    private Date subscribeEndDate;
}
