package untitled.domain;

import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class PointPaymentRequested extends AbstractEvent {

    private Long readerId;
    private Long bookId;
    private Boolean purchase;
}