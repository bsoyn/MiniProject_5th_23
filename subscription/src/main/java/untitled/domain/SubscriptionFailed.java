package untitled.domain;

import lombok.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class SubscriptionFailed extends AbstractEvent {

    private Long readerId;
    private String reason;

    public SubscriptionFailed() {
        super();
    }
}