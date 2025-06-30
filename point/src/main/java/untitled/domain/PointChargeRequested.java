package untitled.domain;

import java.time.LocalDate;
import java.util.*;
import lombok.*;
import untitled.domain.*;
import untitled.infra.AbstractEvent;

@Data
@ToString
public class PointChargeRequested extends AbstractEvent {

    private Long id;
    private Long readerId;
    private Integer point;
    private String impUid;
    private Integer cost;

    public PointChargeRequested(Point aggregate) {
        super(aggregate); 
        this.id = aggregate.getId();
        this.readerId = aggregate.getReaderId();
        this.point = aggregate.getPoint();
        this.impUid = aggregate.getImpUid();
        this.cost = aggregate.getCost();
    }

    public PointChargeRequested() {
        super();
    }
}
