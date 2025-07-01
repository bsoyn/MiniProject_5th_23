package untitled.domain;

import lombok.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class PointUsageRequested extends AbstractEvent {

    private Long readerId;
    private Integer point;
    private Long paymentId;
    private Boolean isCompleted;

    public PointUsageRequested() {
        super();
    }

    public PointUsageRequested(PaymentFinished finished) {
        super(finished);
        this.readerId = finished.getReaderId();
        this.point = finished.getPoint();
        this.paymentId = finished.getId();
        this.isCompleted = finished.getIsCompleted();
    }
}
