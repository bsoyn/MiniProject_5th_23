package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class BuyApproved extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Long bookId;
    private Integer point;
}
