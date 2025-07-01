package untitled.domain;

import lombok.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
@NoArgsConstructor
public class PointUsageRequested extends AbstractEvent {

    private Long readerId;
    private Integer point;
    private Long paymentId;
    private Boolean isCompleted;

    public PointUsageRequested(PaymentFinished finished) {
        super(finished);
        this.readerId = finished.getReaderId();
        this.point = finished.getPoint();
        this.paymentId = finished.getId();
        this.isCompleted = true;
    }

    public PointUsageRequested(PaymentFailed failed) {
        super(failed);
        this.readerId = failed.getReaderId();
        this.point = failed.getPoint();
        this.paymentId = failed.getId();
        this.isCompleted = false;
    }
}
